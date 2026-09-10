package aoc.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate

class StructureProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(GenerateStructure::class.qualifiedName!!)
        val ret = symbols.filter { !it.validate() }.toList()

        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(StructureVisitor(), Unit) }

        return ret
    }

    inner class StructureVisitor : KSVisitorVoid() {
        override fun visitClassDeclaration(
            classDeclaration: KSClassDeclaration,
            data: Unit,
        ) {
            val packageName = classDeclaration.packageName.asString()
            val className = classDeclaration.simpleName.asString()

            // Check if class already has a companion object
            val hasCompanion =
                classDeclaration.declarations.any {
                    it is KSClassDeclaration && it.isCompanionObject
                }

            if (hasCompanion) {
                logger.warn(
                    "@GenerateStructure: $className already has a companion object, skipping generation",
                    classDeclaration,
                )
                return
            }

            // Check for customLine and multiStructure parameters FIRST
            val generateAnnotation =
                classDeclaration.annotations.find {
                    it.shortName.asString() == "GenerateStructure"
                }
            val isCustomLine =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "customLine" }
                    ?.value as? Boolean ?: false
            val isMultiStructure =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "multiStructure" }
                    ?.value as? Boolean ?: false
            val isLineBased =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "lineBased" }
                    ?.value as? Boolean ?: false
            val discriminatorField =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "discriminatorField" }
                    ?.value as? String ?: "type"
            val skipHeaderLines =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "skipHeaderLines" }
                    ?.value as? Int ?: 0
            val skipFooterLines =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "skipFooterLines" }
                    ?.value as? Int ?: 0

            // The generation modes are mutually exclusive, but the dispatch below is a `when`, so
            // without this check a class asking for two of them would silently get whichever branch
            // happens to come first.
            val requestedModes =
                listOf("customLine" to isCustomLine, "multiStructure" to isMultiStructure, "lineBased" to isLineBased)
                    .filter { (_, enabled) -> enabled }
                    .map { (name, _) -> name }
            if (requestedModes.size > 1) {
                logger.error(
                    "@GenerateStructure: ${requestedModes.joinToString(" and ")} are mutually exclusive on $className",
                    classDeclaration,
                )
                return
            }

            if (isMultiStructure && discriminatorField.isBlank()) {
                logger.error(
                    "@GenerateStructure(multiStructure=true) needs a non-blank discriminatorField on $className",
                    classDeclaration,
                )
                return
            }
            if (!isMultiStructure && discriminatorField != "type") {
                logger.warn(
                    "@GenerateStructure: discriminatorField is only used when multiStructure=true, " +
                        "so it has no effect on $className",
                    classDeclaration,
                )
            }

            if (skipHeaderLines < 0 || skipFooterLines < 0) {
                logger.error(
                    "@GenerateStructure: skipHeaderLines/skipFooterLines cannot be negative on $className",
                    classDeclaration,
                )
                return
            }

            // For non-multiStructure classes, check for primary constructor parameters
            val parameters =
                if (!isMultiStructure) {
                    val constructor = classDeclaration.primaryConstructor
                    if (constructor == null) {
                        logger.error("@GenerateStructure class must have a primary constructor", classDeclaration)
                        return
                    }

                    val params = constructor.parameters
                    if (params.isEmpty()) {
                        logger.error("@GenerateStructure class must have at least one parameter", classDeclaration)
                        return
                    }
                    params
                } else {
                    emptyList()
                }

            // @FromMatch describes sources only the customLine and lineBased templates can read; the
            // standard and multi templates would silently ignore it.
            if (!isCustomLine && !isLineBased) {
                parameters.filter { matchPartOf(it) != null }.forEach { param ->
                    logger.error(
                        "@FromMatch on '${param.name?.asString()}' requires " +
                            "@GenerateStructure(customLine=true) or (lineBased=true)",
                        param,
                    )
                }
            }

            // The sealed subclasses have to be resolved before the file is created: they contribute
            // both to the generated body and to the set of source files it depends on.
            val sealedSubclasses =
                if (isMultiStructure) {
                    classDeclaration.getSealedSubclasses().toList().also { subclasses ->
                        if (subclasses.isEmpty()) {
                            logger.error(
                                "@GenerateStructure(multiStructure=true) requires a sealed class with subclasses",
                                classDeclaration,
                            )
                        }
                    }
                } else {
                    emptyList()
                }
            if (isMultiStructure && sealedSubclasses.isEmpty()) return

            // Isolating, not aggregating: each companion depends only on the file declaring the
            // annotated class, plus — for sealed hierarchies — the files declaring its subclasses.
            val sourceFiles: List<KSFile> =
                (listOf(classDeclaration.containingFile!!) + sealedSubclasses.mapNotNull { it.containingFile })
                    .distinct()

            val file =
                codeGenerator.createNewFile(
                    dependencies = Dependencies(aggregating = false, sources = sourceFiles.toTypedArray()),
                    packageName = packageName,
                    fileName = "${className}Companion",
                )

            val skips = skipOverrides(skipHeaderLines, skipFooterLines)

            val companionSource =
                when {
                    isMultiStructure ->
                        generateMultiStructureCompanion(
                            packageName,
                            className,
                            sealedSubclasses,
                            discriminatorField,
                            skips,
                        )

                    isCustomLine -> generateCustomLineCompanion(packageName, className, parameters, skips)

                    isLineBased -> generateLineBasedCompanion(packageName, className, parameters, skips)

                    else -> generateStandardCompanion(packageName, className, parameters, skips)
                }

            file.bufferedWriter().use { writer -> writer.write(companionSource) }

            val companionType =
                when {
                    isMultiStructure -> "IStructureMulti"
                    isCustomLine -> "IStructureCustomLine"
                    isLineBased -> "IStructureLine"
                    else -> "IStructure"
                }
            logger.info("Generated $companionType companion for $packageName.$className")
        }

        private fun generateStandardCompanion(
            packageName: String,
            className: String,
            parameters: List<KSValueParameter>,
            skips: String,
        ): String =
            """
                |package $packageName
                |
                |import aoc.ksp.BaseEntity
                |import aoc.ksp.IStructure${converterImports(parameters)}
                |
                |/**
                | * Generated by KSP StructureProcessor
                | * Standalone companion object that implements IStructure<$className>
                | *
                | * Supported field types:
                | * - Int, String, Char
                | * - Nullable variants of all types above
                | * - Custom types with @FieldConverter annotation
                | *
                | * Usage: ${className}Companion.fromLine(line, regex)
                | * Example:
                | *   val regex = Regex("(?<field1>\\d+) (?<field2>\\w+)")
                | *   val entity = ${className}Companion.fromLine("123 abc", regex)
                | *
                | * Note: Regex named groups must match field names exactly
                | */
                |object ${className}Companion : IStructure<$className> {$skips
                |    override fun create(collection: MatchGroupCollection): $className =
                |        $className(
                |${generateParameterMappings(parameters)},
                |        )
                |}
                |
            """.trimMargin()

        private fun generateCustomLineCompanion(
            packageName: String,
            className: String,
            parameters: List<KSValueParameter>,
            skips: String,
        ): String =
            """
                |package $packageName
                |
                |import aoc.ksp.IStructureCustomLine
                |
                |/**
                | * Generated by KSP StructureProcessor
                | * Standalone companion object that implements IStructureCustomLine<$className>
                | *
                | * This companion processes the entire line and match sequence,
                | * allowing for custom parsing logic that goes beyond simple field extraction.
                | *
                | * Usage: ${className}Companion.fromLine(line, regex)
                | * Example:
                | *   val regex = Regex("[A-Z][a-z]?")
                | *   val entity = ${className}Companion.fromLine("CaRnAlBSi", regex)
                | *
                | * Note: The create method receives both the line and all matches
                | */
                |object ${className}Companion : IStructureCustomLine<$className> {$skips
                |    override fun create(
                |        line: String,
                |        collection: Sequence<MatchResult>,
                |    ): $className =
                |        $className(
                |${generateCustomLineParameterMappings(parameters)},
                |        )
                |}
                |
            """.trimMargin()

        private fun generateLineBasedCompanion(
            packageName: String,
            className: String,
            parameters: List<KSValueParameter>,
            skips: String,
        ): String =
            """
                |package $packageName
                |
                |import aoc.ksp.BaseEntity
                |import aoc.ksp.IStructureLine${converterImports(parameters)}
                |
                |/**
                | * Generated by KSP StructureProcessor
                | * Standalone companion object that implements IStructureLine<$className>
                | *
                | * This companion finds all regex matches in a line and creates a list of entities.
                | * Useful for parsing multiple occurrences of a pattern in a single line.
                | *
                | * Supported field types:
                | * - Int, String, Char
                | * - Nullable variants of all types above
                | * - IntRange annotated @FromMatch(MatchPart.RANGE), mapped to the match's own range
                | * - Custom types with @FieldConverter annotation
                | *
                | * Usage: ${className}Companion.fromLine(line, regex)
                | * Example:
                | *   val regex = Regex("(?<direction>[LR])(?<steps>\\d+)")
                | *   val instructions = ${className}Companion.fromLine("R3, L5, R2", regex)
                | *
                | * Note: Regex is applied with findAll() to find every match in the line
                | * Note: Regex named groups must match field names exactly
                | */
                |object ${className}Companion : IStructureLine<$className> {$skips
                |    override fun create(collection: MatchResult): $className =
                |        $className(
                |${generateLineBasedParameterMappings(parameters)},
                |        )
                |}
                |
            """.trimMargin()

        private fun generateMultiStructureCompanion(
            packageName: String,
            className: String,
            sealedSubclasses: List<KSClassDeclaration>,
            discriminatorField: String,
            skips: String,
        ): String {
            val subclassParameters =
                sealedSubclasses.flatMap { it.primaryConstructor?.parameters.orEmpty() }

            // Resolved once, so the documented mapping and the generated `when` cases cannot drift.
            val tokenBySubclass = resolveDiscriminatorTokens(sealedSubclasses)

            return """
                |package $packageName
                |
                |import aoc.ksp.BaseEntity
                |import aoc.ksp.IStructureMulti${converterImports(subclassParameters)}
                |
                |/**
                | * Generated by KSP StructureProcessor
                | * Standalone companion object that implements IStructureMulti<$className>
                | *
                | * Routes to the appropriate sealed subclass based on discriminator field '$discriminatorField'.
                | *
                | * Discriminator mapping (uppercased group value -> subclass); the token is the
                | * subclass name unless @StructureName overrides it, so a rename changes what the
                | * subclass answers to:
                |${tokenBySubclass.entries.joinToString("\n") { (subclass, token) ->
                " * - \"$token\" -> ${subclass.simpleName.asString()}"
            }}
                | *
                | * Usage: ${className}Companion.fromLine(line, regexArray)
                | * Example:
                | *   val regexArray = arrayOf(
                | *       Regex("(?<cmd>jmp) (?<offset>[+-]\\d+)"),
                | *       Regex("(?<cmd>inc) (?<register>[a-z]+)")
                | *   )
                | *   val instruction = ${className}Companion.fromLine("jmp +10", regexArray)
                | *
                | * Note: Regex named groups must match field names exactly
                | * Note: Discriminator field '$discriminatorField' is used to determine the subclass
                | */
                |object ${className}Companion : IStructureMulti<$className> {$skips
                |    override fun create(collection: MatchGroupCollection): $className {
                |        val discriminator = BaseEntity.getAsString(collection, "$discriminatorField").uppercase()
                |        return when (discriminator) {
                |${generateSealedSubclassCases(tokenBySubclass, className)}
                |
                |            else -> {
                |                throw IllegalArgumentException("Unknown discriminator value: ${"$"}discriminator in $className creation")
                |            }
                |        }
                |    }
                |}
                |
                """.trimMargin()
        }

        /**
         * `override val` lines for the non-zero skip counts, formatted so they can be spliced into a
         * companion body. Zero is the [IStructureSkips] default, so an entity that asks for no
         * trimming generates exactly what it generated before the skips existed.
         */
        private fun skipOverrides(
            skipHeaderLines: Int,
            skipFooterLines: Int,
        ): String {
            val overrides =
                buildList {
                    if (skipHeaderLines > 0) add("skipHeaderLines" to skipHeaderLines)
                    if (skipFooterLines > 0) add("skipFooterLines" to skipFooterLines)
                }

            if (overrides.isEmpty()) return ""
            return overrides.joinToString(
                separator = "",
                prefix = "\n",
            ) { (name, count) -> "|    override val $name: Int = $count\n" } + "|"
        }

        /**
         * Import lines for every distinct [FieldConverter] referenced by [parameters], formatted so
         * they can be appended directly after the last fixed import in a codegen template.
         */
        private fun converterImports(parameters: List<KSValueParameter>): String {
            val converters =
                parameters
                    .mapNotNull { param ->
                        converterTypeOf(param)?.declaration?.qualifiedName?.asString()
                    }.toSet()

            if (converters.isEmpty()) return ""
            return "\n|${converters.joinToString("\n") { "import $it" }}"
        }

        /**
         * The `MatchPart` name an explicit `@FromMatch` asks for, or `null` when the parameter is
         * read from a named group like every other one.
         */
        private fun matchPartOf(param: KSValueParameter): String? {
            val part =
                param.annotations
                    .find { annotation -> annotation.shortName.asString() == "FromMatch" }
                    ?.arguments
                    ?.find { it.name?.asString() == "part" }
                    ?.value ?: return null

            return when (part) {
                is KSType -> part.declaration.simpleName.asString()
                is KSClassDeclaration -> part.simpleName.asString()
                else -> part.toString().substringAfterLast('.')
            }
        }

        private fun converterTypeOf(param: KSValueParameter): KSType? =
            param.annotations
                .find { annotation -> annotation.shortName.asString() == "FieldConverter" }
                ?.arguments
                ?.find { it.name?.asString() == "converter" }
                ?.value as? KSType

        /**
         * Custom-line companions get the raw line plus the whole match sequence, neither of which is
         * a named group, so every parameter has to say where it comes from via `@FromMatch`.
         */
        private fun generateCustomLineParameterMappings(parameters: List<KSValueParameter>): String =
            parameters.joinToString(",\n") { param ->
                val name = param.name?.asString() ?: "unknown"
                val type = param.type.resolve()
                val typeString = type.declaration.simpleName.asString()

                val expression =
                    when (val part = matchPartOf(param)) {
                        "LINE" ->
                            if (typeString == "String") {
                                "line"
                            } else {
                                logger.error(
                                    "@FromMatch(LINE) needs a String parameter, but '$name' is '$typeString'",
                                    param,
                                )
                                "TODO(\"$name\")"
                            }

                        "ALL_MATCHES" ->
                            if (typeString == "List") {
                                val innerType =
                                    type.arguments
                                        .firstOrNull()
                                        ?.type
                                        ?.resolve()
                                        ?.declaration
                                        ?.simpleName
                                        ?.asString()
                                if (innerType == null) {
                                    logger.error(
                                        "@FromMatch(ALL_MATCHES) needs a List<T> element type on '$name'",
                                        param,
                                    )
                                    "TODO(\"$name\")"
                                } else {
                                    // The element type is constructed from the match text, so it needs a
                                    // constructor taking a single String.
                                    "collection.toList().map { $innerType(it.value) }"
                                }
                            } else {
                                logger.error(
                                    "@FromMatch(ALL_MATCHES) needs a List parameter, but '$name' is '$typeString'",
                                    param,
                                )
                                "TODO(\"$name\")"
                            }

                        else -> {
                            val detail =
                                if (part == null) {
                                    "is missing @FromMatch"
                                } else {
                                    "uses @FromMatch($part), which is not valid here"
                                }
                            logger.error(
                                "@GenerateStructure(customLine=true): parameter '$name' $detail; " +
                                    "use MatchPart.LINE or MatchPart.ALL_MATCHES",
                                param,
                            )
                            "TODO(\"$name\")"
                        }
                    }

                "            $name = $expression"
            }

        private fun generateParameterMappings(parameters: List<KSValueParameter>): String =
            parameters.joinToString(",\n") { param ->
                "            ${param.name?.asString() ?: "unknown"} = ${getterExpression(param)}"
            }

        /**
         * Line-based companions receive a [MatchResult] rather than a [MatchGroupCollection], so the
         * accessors read from `collection.groups`. A parameter annotated `@FromMatch(RANGE)` is mapped
         * to the match's own position in the line, which has no equivalent named group.
         */
        private fun generateLineBasedParameterMappings(parameters: List<KSValueParameter>): String =
            parameters.joinToString(",\n") { param ->
                val name = param.name?.asString() ?: "unknown"
                val typeString =
                    param.type
                        .resolve()
                        .declaration.simpleName
                        .asString()

                val expression =
                    when (val part = matchPartOf(param)) {
                        null -> getterExpression(param, receiver = "collection.groups")

                        "RANGE" ->
                            if (typeString == "IntRange") {
                                "collection.range"
                            } else {
                                logger.error(
                                    "@FromMatch(RANGE) needs an IntRange parameter, but '$name' is '$typeString'",
                                    param,
                                )
                                "TODO(\"$name\")"
                            }

                        else -> {
                            logger.error(
                                "@FromMatch($part) is not valid with lineBased=true; parameter '$name' " +
                                    "can only use MatchPart.RANGE or a named group",
                                param,
                            )
                            "TODO(\"$name\")"
                        }
                    }

                "            $name = $expression"
            }

        /**
         * The discriminator token each sealed subclass answers to.
         *
         * The default is the subclass simple name uppercased (Hlf -> HLF), which matches the runtime
         * discriminator because [generateMultiStructureCompanion] uppercases the group value too. An
         * optional `@StructureName("s")` overrides it, so a readable class name (Spin) can be matched
         * by the short token the input actually carries ("s").
         */
        private fun resolveDiscriminatorTokens(subclasses: List<KSClassDeclaration>): Map<KSClassDeclaration, String> {
            val tokens = LinkedHashMap<KSClassDeclaration, String>()

            subclasses.forEach { subclass ->
                val subclassName = subclass.simpleName.asString()
                val alias =
                    subclass.annotations
                        .firstOrNull { it.shortName.asString() == "StructureName" }
                        ?.arguments
                        ?.find { it.name?.asString() == "value" }
                        ?.value as? String

                val token =
                    if (alias != null && alias.isBlank()) {
                        logger.error(
                            "@StructureName on $subclassName must not be blank, falling back to the class name",
                            subclass,
                        )
                        subclassName.uppercase()
                    } else {
                        (alias ?: subclassName).uppercase()
                    }

                // Two subclasses answering to one token is a silent wrong answer at runtime: the
                // `when` picks the first case and the second is unreachable.
                tokens.entries.find { it.value == token }?.let { clash ->
                    logger.error(
                        "Discriminator token \"$token\" is already used by ${clash.key.simpleName.asString()}. " +
                            "Give $subclassName a distinct @StructureName value.",
                        subclass,
                    )
                }

                tokens[subclass] = token
            }

            return tokens
        }

        private fun generateSealedSubclassCases(
            tokenBySubclass: Map<KSClassDeclaration, String>,
            className: String,
        ): String =
            tokenBySubclass.entries.joinToString("\n\n") { (subclass, discriminatorValue) ->
                val subclassName = subclass.simpleName.asString()

                val parameters = subclass.primaryConstructor?.parameters ?: emptyList()

                // Parameters with a default value are left to the constructor unless an explicit
                // converter says how to read them, so subclasses can carry state the regex never sets.
                val paramsList =
                    parameters.mapNotNull { param ->
                        if (param.hasDefault && converterTypeOf(param) == null) {
                            null
                        } else {
                            "                    ${param.name!!.asString()} = ${getterExpression(param)}"
                        }
                    }

                val parameterMappings =
                    when {
                        // An object or a parameterless subclass is referenced by name alone.
                        parameters.isEmpty() -> ""
                        // Every parameter was defaulted away, but the constructor still needs a call.
                        paramsList.isEmpty() -> "()"
                        else -> "(\n${paramsList.joinToString(",\n")},\n                )"
                    }

                "            \"$discriminatorValue\" -> {\n                $className.$subclassName$parameterMappings\n            }"
            }

        /**
         * The single source of truth for turning a constructor parameter into the expression that
         * produces its value. Used for both standard entities and sealed subclass branches so the
         * two paths cannot drift apart in supported types or converter handling.
         */
        private fun getterExpression(
            param: KSValueParameter,
            receiver: String = "collection",
        ): String {
            val name = param.name?.asString() ?: "unknown"

            converterTypeOf(param)?.let { converterType ->
                val converterName = converterType.declaration.simpleName.asString()
                return "$converterName.convert($receiver, \"$name\")"
            }

            val type = param.type.resolve()
            val typeString = type.declaration.simpleName.asString()
            val isNullable = type.isMarkedNullable

            // Kept deliberately in step with the getters BaseEntity actually declares: anything wider
            // would generate calls that fail to resolve inside generated code instead of here.
            val supported = setOf("Int", "String", "Char")

            if (typeString !in supported) {
                val rendered = "$typeString${if (isNullable) "?" else ""}"
                logger.error(
                    "Unsupported type: $rendered for parameter $name. " +
                        "Supported types: ${supported.joinToString(", ")} (and nullable variants). " +
                        "For custom types, use @FieldConverter annotation with a TypeConverter implementation.",
                    param,
                )
                // Return a placeholder that will cause a compile error with a clear message
                return "TODO(\"Add @FieldConverter for $rendered or add support in BaseEntity\")"
            }

            val getter = if (isNullable) "getAsNullable$typeString" else "getAs$typeString"
            return "BaseEntity.$getter($receiver, \"$name\")"
        }
    }
}

/**
 * Provider for StructureProcessor
 */
class StructureProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        StructureProcessor(environment.codeGenerator, environment.logger)
}
