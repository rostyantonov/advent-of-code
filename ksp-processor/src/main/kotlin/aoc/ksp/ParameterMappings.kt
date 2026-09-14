package aoc.ksp

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * The parameter types [BaseEntity] declares getters for.
 *
 * Single source of truth: [ParameterMappings.getterExpression] checks against it and
 * [CompanionTemplates] documents it in the generated KDoc, so the two cannot drift.
 */
internal val SUPPORTED_TYPES = listOf("Int", "Long", "Boolean", "String", "Char")

/**
 * Turns constructor parameters into the expressions that produce their values.
 *
 * Every generation mode routes through here, so the supported types and the converter handling
 * cannot differ between a standard entity and a sealed subclass branch.
 */
internal class ParameterMappings(
    private val logger: KSPLogger,
) {
    /**
     * The single source of truth for turning a constructor parameter into the expression that
     * produces its value.
     */
    fun getterExpression(
        param: KSValueParameter,
        receiver: String = "collection",
    ): String {
        val name = param.name?.asString() ?: "unknown"

        converterTypeOf(param)?.let { converterType ->
            val converterName = converterType.declaration.simpleName.asString()
            reportConverterMismatch(param, converterType)
            return "$converterName.convert($receiver, \"$name\")"
        }

        val type = param.type.resolve()
        val typeString = type.declaration.simpleName.asString()
        val isNullable = type.isMarkedNullable

        // Any enum is supported, because the getter is generic: the constant is matched by name
        // against the group value rather than by a getter BaseEntity has to declare per type.
        if (enumDeclarationOf(param) != null) {
            val getter = if (isNullable) "getAsNullableEnum" else "getAsEnum"
            return "BaseEntity.$getter<$typeString>($receiver, \"$name\")"
        }

        if (typeString !in SUPPORTED_TYPES) {
            val rendered = "$typeString${if (isNullable) "?" else ""}"
            logger.error(
                "Unsupported type: $rendered for parameter $name. " +
                    "Supported types: ${SUPPORTED_TYPES.joinToString(", ")}, any enum " +
                    "(and nullable variants). " +
                    "For custom types, use @FieldConverter annotation with a TypeConverter implementation.",
                param,
            )
            // Return a placeholder that will cause a compile error with a clear message
            return "TODO(\"Add @FieldConverter for $rendered or add support in BaseEntity\")"
        }

        val getter = if (isNullable) "getAsNullable$typeString" else "getAs$typeString"
        return "BaseEntity.$getter($receiver, \"$name\")"
    }

    /**
     * Reports a `@FieldConverter` whose `TypeConverter<T>` produces something other than the
     * parameter's own type.
     *
     * Without this the generated call simply fails to typecheck, and the error is reported against
     * a line in `build/generated` rather than against the annotation that is wrong.
     */
    private fun reportConverterMismatch(
        param: KSValueParameter,
        converterType: KSType,
    ) {
        val declaration = converterType.declaration as? KSClassDeclaration ?: return

        val produced =
            declaration.superTypes
                .map { it.resolve() }
                .firstOrNull { it.declaration.simpleName.asString() == "TypeConverter" }
                ?.arguments
                ?.firstOrNull()
                ?.type
                ?.resolve()
                ?.declaration
                ?.qualifiedName
                ?.asString() ?: return

        val expected =
            param.type
                .resolve()
                .declaration.qualifiedName
                ?.asString() ?: return

        if (produced != expected) {
            logger.error(
                "@FieldConverter(${declaration.simpleName.asString()}) produces " +
                    "${produced.substringAfterLast('.')}, but '${param.name?.asString()}' is " +
                    expected.substringAfterLast('.'),
                param,
            )
        }
    }

    /**
     * The enum class a parameter is declared as, or null when it is anything else.
     *
     * A parameter carrying an explicit `@FieldConverter` is never treated as an enum: the converter
     * is the author saying the default name matching is not what this field needs, which is how
     * `BitOperationConverter` keeps its DIRECT fallback for an absent group.
     */
    fun enumDeclarationOf(param: KSValueParameter): KSClassDeclaration? {
        if (converterTypeOf(param) != null) return null

        val declaration = param.type.resolve().declaration
        return (declaration as? KSClassDeclaration)?.takeIf { it.classKind == ClassKind.ENUM_CLASS }
    }

    /** The [TypeConverter] a parameter's `@FieldConverter` names, or null when it has none. */
    fun converterTypeOf(param: KSValueParameter): KSType? =
        param.annotationNamed("FieldConverter")?.argument<KSType>("converter")

    /**
     * The `MatchPart` name an explicit `@FromMatch` asks for, or `null` when the parameter is read
     * from a named group like every other one.
     */
    fun matchPartOf(param: KSValueParameter): String? {
        val part =
            param
                .annotationNamed("FromMatch")
                ?.arguments
                ?.find { it.name?.asString() == "part" }
                ?.value
                ?: return null

        return when (part) {
            is KSType -> part.declaration.simpleName.asString()
            is KSClassDeclaration -> part.simpleName.asString()
            else -> part.toString().substringAfterLast('.')
        }
    }

    fun standard(parameters: List<KSValueParameter>): String =
        parameters.joinToString(",\n") { param ->
            "            ${param.name?.asString() ?: "unknown"} = ${getterExpression(param)}"
        }

    /**
     * Custom-line companions get the raw line plus the whole match sequence, neither of which is a
     * named group, so every parameter has to say where it comes from via `@FromMatch`.
     */
    fun customLine(parameters: List<KSValueParameter>): String =
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

                    "ALL_MATCHES" -> allMatchesExpression(param, name, type, typeString)

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

    /**
     * Line-based companions receive a `MatchResult` rather than a `MatchGroupCollection`, so the
     * accessors read from `collection.groups`. A parameter annotated `@FromMatch(RANGE)` is mapped
     * to the match's own position in the line, which has no equivalent named group.
     */
    fun lineBased(parameters: List<KSValueParameter>): String =
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
     * discriminator because the generated `create` uppercases the group value too. An optional
     * `@StructureName("s")` overrides it, so a readable class name (Spin) can be matched by the
     * short token the input actually carries ("s").
     */
    fun discriminatorTokens(subclasses: List<KSClassDeclaration>): Map<KSClassDeclaration, String> {
        val tokens = LinkedHashMap<KSClassDeclaration, String>()

        subclasses.forEach { subclass ->
            val subclassName = subclass.simpleName.asString()
            val alias = subclass.annotationNamed("StructureName")?.argument<String>("value")

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

    fun sealedSubclassCases(
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

    private fun allMatchesExpression(
        param: KSValueParameter,
        name: String,
        type: KSType,
        typeString: String,
    ): String {
        if (typeString != "List") {
            logger.error(
                "@FromMatch(ALL_MATCHES) needs a List parameter, but '$name' is '$typeString'",
                param,
            )
            return "TODO(\"$name\")"
        }

        val element =
            type.arguments
                .firstOrNull()
                ?.type
                ?.resolve()
                ?.declaration
                ?: run {
                    logger.error("@FromMatch(ALL_MATCHES) needs a List<T> element type on '$name'", param)
                    return "TODO(\"$name\")"
                }

        val innerType = element.simpleName.asString()

        // The element is constructed straight from the match text, so a constructor taking one
        // String is the whole contract - checked here so a mismatch is reported on the parameter
        // rather than inside the generated file.
        val constructor = (element as? KSClassDeclaration)?.primaryConstructor
        val takesOneString =
            constructor
                ?.parameters
                ?.singleOrNull()
                ?.type
                ?.resolve()
                ?.declaration
                ?.simpleName
                ?.asString() == "String"

        if (!takesOneString) {
            logger.error(
                "@FromMatch(ALL_MATCHES) builds each element from the match text, so $innerType needs " +
                    "a primary constructor taking a single String",
                param,
            )
            return "TODO(\"$name\")"
        }

        return "collection.toList().map { $innerType(it.value) }"
    }
}
