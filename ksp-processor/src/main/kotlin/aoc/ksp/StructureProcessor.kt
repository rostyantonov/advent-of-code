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
import java.io.Writer

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
            val discriminatorField =
                generateAnnotation
                    ?.arguments
                    ?.find { it.name?.asString() == "discriminatorField" }
                    ?.value as? String ?: "type"

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

            file.bufferedWriter().use { writer ->
                when {
                    isMultiStructure -> {
                        generateMultiStructureCompanion(
                            writer,
                            packageName,
                            className,
                            sealedSubclasses,
                            discriminatorField,
                        )
                    }

                    isCustomLine -> {
                        generateCustomLineCompanion(writer, packageName, className, parameters)
                    }

                    else -> {
                        generateStandardCompanion(writer, packageName, className, parameters)
                    }
                }
            }

            val companionType =
                when {
                    isMultiStructure -> "IStructureMulti"
                    isCustomLine -> "IStructureCustomLine"
                    else -> "IStructure"
                }
            logger.info("Generated $companionType companion for $packageName.$className")
        }

        private fun generateStandardCompanion(
            writer: Writer,
            packageName: String,
            className: String,
            parameters: List<KSValueParameter>,
        ) {
            writer.write(
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
                | * - Int, Long, String, Char, UShort
                | * - Double, Float, Boolean, Byte, Short
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
                |object ${className}Companion : IStructure<$className> {
                |    override fun create(collection: MatchGroupCollection): $className =
                |        $className(
                |${generateParameterMappings(parameters)},
                |        )
                |}
                |
                """.trimMargin(),
            )
        }

        private fun generateCustomLineCompanion(
            writer: Writer,
            packageName: String,
            className: String,
            parameters: List<KSValueParameter>,
        ) {
            writer.write(
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
                |object ${className}Companion : IStructureCustomLine<$className> {
                |    override fun create(
                |        line: String,
                |        collection: Sequence<MatchResult>,
                |    ): $className =
                |        $className(
                |${generateCustomLineParameterMappings(parameters)},
                |        )
                |}
                |
                """.trimMargin(),
            )
        }

        private fun generateMultiStructureCompanion(
            writer: Writer,
            packageName: String,
            className: String,
            sealedSubclasses: List<KSClassDeclaration>,
            discriminatorField: String,
        ) {
            val subclassParameters =
                sealedSubclasses.flatMap { it.primaryConstructor?.parameters.orEmpty() }

            writer.write(
                """
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
                | * Supported subclasses:
                |${sealedSubclasses.joinToString("\n") { " * - ${it.simpleName.asString()}" }}
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
                |object ${className}Companion : IStructureMulti<$className> {
                |    override fun create(collection: MatchGroupCollection): $className {
                |        val discriminator = BaseEntity.getAsString(collection, "$discriminatorField").uppercase()
                |        return when (discriminator) {
                |${generateSealedSubclassCases(sealedSubclasses, className)}
                |
                |            else -> {
                |                throw IllegalArgumentException("Unknown discriminator value: ${"$"}discriminator in $className creation")
                |            }
                |        }
                |    }
                |}
                |
                """.trimMargin(),
            )
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

        private fun converterTypeOf(param: KSValueParameter): KSType? =
            param.annotations
                .find { annotation -> annotation.shortName.asString() == "FieldConverter" }
                ?.arguments
                ?.find { it.name?.asString() == "converter" }
                ?.value as? KSType

        private fun generateCustomLineParameterMappings(parameters: List<KSValueParameter>): String {
            // For custom line processing, we expect specific parameter patterns
            // The first parameter is typically the line itself, and subsequent parameters
            // are derived from the collection
            return parameters.joinToString(",\n") { param ->
                val name = param.name?.asString() ?: "unknown"
                val type = param.type.resolve()
                val typeString = type.declaration.simpleName.asString()

                when {
                    typeString == "String" && name == "stringValue" -> {
                        "            $name = line"
                    }

                    typeString == "List" -> {
                        // For List types, we convert the collection
                        val typeArg =
                            type.arguments
                                .firstOrNull()
                                ?.type
                                ?.resolve()
                        val innerType = typeArg?.declaration?.simpleName?.asString() ?: "Unknown"
                        "            $name = collection.toList().map { $innerType(it.value) }"
                    }

                    else -> {
                        logger.warn(
                            "Custom line parameter '$name' of type '$typeString' may need manual mapping",
                            param,
                        )
                        "            $name = TODO(\"Map $name from line or collection\")"
                    }
                }
            }
        }

        private fun generateParameterMappings(parameters: List<KSValueParameter>): String =
            parameters.joinToString(",\n") { param ->
                "            ${param.name?.asString() ?: "unknown"} = ${getterExpression(param)}"
            }

        private fun generateSealedSubclassCases(
            subclasses: List<KSClassDeclaration>,
            className: String,
        ): String =
            subclasses.joinToString("\n\n") { subclass ->
                val subclassName = subclass.simpleName.asString()
                // Map subclass name to uppercase for discriminator (e.g., Hlf -> HLF, Jmp -> JMP)
                val discriminatorValue = subclassName.uppercase()

                val parameters = subclass.primaryConstructor?.parameters ?: emptyList()

                val parameterMappings =
                    if (parameters.isEmpty()) {
                        ""
                    } else {
                        val paramsList =
                            parameters.joinToString(",\n") { param ->
                                "                    ${param.name!!.asString()} = ${getterExpression(param)}"
                            }
                        "(\n$paramsList,\n                )"
                    }

                "            \"$discriminatorValue\" -> {\n                $className.$subclassName$parameterMappings\n            }"
            }

        /**
         * The single source of truth for turning a constructor parameter into the expression that
         * produces its value. Used for both standard entities and sealed subclass branches so the
         * two paths cannot drift apart in supported types or converter handling.
         */
        private fun getterExpression(param: KSValueParameter): String {
            val name = param.name?.asString() ?: "unknown"

            converterTypeOf(param)?.let { converterType ->
                val converterName = converterType.declaration.simpleName.asString()
                return "$converterName.convert(collection, \"$name\")"
            }

            val type = param.type.resolve()
            val typeString = type.declaration.simpleName.asString()
            val isNullable = type.isMarkedNullable

            val supported =
                setOf("Int", "Long", "String", "Char", "UShort", "Double", "Float", "Boolean", "Byte", "Short")

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
            return "BaseEntity.$getter(collection, \"$name\")"
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
