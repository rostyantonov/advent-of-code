package aoc.common.ksp

import aoc.common.entity.FieldConverter
import aoc.common.entity.GenerateStructure
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
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
                logger.warn("@GenerateStructure: $className already has a companion object, skipping generation", classDeclaration)
                return
            }

            // Get primary constructor parameters
            val constructor = classDeclaration.primaryConstructor
            if (constructor == null) {
                logger.error("@GenerateStructure class must have a primary constructor", classDeclaration)
                return
            }

            val parameters = constructor.parameters
            if (parameters.isEmpty()) {
                logger.error("@GenerateStructure class must have at least one parameter", classDeclaration)
                return
            }

            // Check for customLine parameter
            val generateAnnotation =
                classDeclaration.annotations.find {
                    it.shortName.asString() == "GenerateStructure"
                }
            val isCustomLine =
                generateAnnotation?.arguments
                    ?.find { it.name?.asString() == "customLine" }
                    ?.value as? Boolean ?: false

            // Generate the companion object implementation
            val file =
                codeGenerator.createNewFile(
                    dependencies = Dependencies(true, classDeclaration.containingFile!!),
                    packageName = packageName,
                    fileName = "${className}Companion",
                )

            file.bufferedWriter().use { writer ->
                if (isCustomLine) {
                    generateCustomLineCompanion(writer, packageName, className, parameters)
                } else {
                    generateStandardCompanion(writer, packageName, className, parameters)
                }
            }

            logger.info("Generated ${if (isCustomLine) "IStructureCustomLine" else "IStructure"} companion for $packageName.$className")
        }

        private fun generateStandardCompanion(
            writer: java.io.Writer,
            packageName: String,
            className: String,
            parameters: List<com.google.devtools.ksp.symbol.KSValueParameter>,
        ) {
            // Collect custom converters needed
            val customConverters =
                parameters
                    .mapNotNull { param ->
                        param.annotations
                            .find { annotation ->
                                annotation.shortName.asString() == "FieldConverter"
                            }?.let { annotation ->
                                val converterType =
                                    annotation.arguments
                                        .find { it.name?.asString() == "converter" }
                                        ?.value as? KSType
                                converterType?.declaration?.qualifiedName?.asString()
                            }
                    }.toSet()

            writer.write(
                """
                |@file:Suppress("unused")
                |
                |package $packageName
                |
                |import aoc.common.entity.BaseEntity
                |import aoc.common.entity.IStructure${if (customConverters.isEmpty()) {
                    ""
                } else {
                    "\n|${customConverters.joinToString(
                        "\n",
                    ) { "import $it" }}"
                }}
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
            writer: java.io.Writer,
            packageName: String,
            className: String,
            parameters: List<com.google.devtools.ksp.symbol.KSValueParameter>,
        ) {
            writer.write(
                """
                |@file:Suppress("unused")
                |
                |package $packageName
                |
                |import aoc.common.entity.IStructureCustomLine
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

        private fun generateCustomLineParameterMappings(parameters: List<com.google.devtools.ksp.symbol.KSValueParameter>): String {
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
                        val typeArg = type.arguments.firstOrNull()?.type?.resolve()
                        val innerType = typeArg?.declaration?.simpleName?.asString() ?: "Unknown"
                        "            $name = collection.toList().map { $innerType(it.value) }"
                    }
                    else -> {
                        logger.warn("Custom line parameter '$name' of type '$typeString' may need manual mapping", param)
                        "            $name = TODO(\"Map $name from line or collection\")"
                    }
                }
            }
        }

        private fun generateParameterMappings(parameters: List<com.google.devtools.ksp.symbol.KSValueParameter>): String =
            parameters.joinToString(",\n") { param ->
                val name = param.name?.asString() ?: "unknown"

                // Check for custom converter annotation
                val customConverter =
                    param.annotations.find { annotation ->
                        annotation.shortName.asString() == "FieldConverter"
                    }

                if (customConverter != null) {
                    val converterType =
                        customConverter.arguments
                            .find { it.name?.asString() == "converter" }
                            ?.value as? KSType
                    val converterName = converterType?.declaration?.simpleName?.asString() ?: "UnknownConverter"
                    return@joinToString "            $name = $converterName.convert(collection, \"$name\")"
                }

                val type = param.type.resolve()
                val typeString = type.declaration.simpleName.asString()
                val isNullable = type.isMarkedNullable

                val getter =
                    when {
                        typeString == "Int" && !isNullable -> {
                            "BaseEntity.getAsInt(collection, \"$name\")"
                        }

                        typeString == "Int" && isNullable -> {
                            "BaseEntity.getAsNullableInt(collection, \"$name\")"
                        }

                        typeString == "String" && !isNullable -> {
                            "BaseEntity.getAsString(collection, \"$name\")"
                        }

                        typeString == "String" && isNullable -> {
                            "BaseEntity.getAsNullableString(collection, \"$name\")"
                        }

                        typeString == "Long" && !isNullable -> {
                            "BaseEntity.getAsLong(collection, \"$name\")"
                        }

                        typeString == "Long" && isNullable -> {
                            "BaseEntity.getAsNullableLong(collection, \"$name\")"
                        }

                        typeString == "Char" && !isNullable -> {
                            "BaseEntity.getAsChar(collection, \"$name\")"
                        }

                        typeString == "Char" && isNullable -> {
                            "BaseEntity.getAsNullableChar(collection, \"$name\")"
                        }

                        typeString == "UShort" && !isNullable -> {
                            "BaseEntity.getAsUShort(collection, \"$name\")"
                        }

                        typeString == "UShort" && isNullable -> {
                            "BaseEntity.getAsNullableUShort(collection, \"$name\")"
                        }

                        typeString == "Double" && !isNullable -> {
                            "BaseEntity.getAsDouble(collection, \"$name\")"
                        }

                        typeString == "Double" && isNullable -> {
                            "BaseEntity.getAsNullableDouble(collection, \"$name\")"
                        }

                        typeString == "Float" && !isNullable -> {
                            "BaseEntity.getAsFloat(collection, \"$name\")"
                        }

                        typeString == "Float" && isNullable -> {
                            "BaseEntity.getAsNullableFloat(collection, \"$name\")"
                        }

                        typeString == "Boolean" && !isNullable -> {
                            "BaseEntity.getAsBoolean(collection, \"$name\")"
                        }

                        typeString == "Boolean" && isNullable -> {
                            "BaseEntity.getAsNullableBoolean(collection, \"$name\")"
                        }

                        typeString == "Byte" && !isNullable -> {
                            "BaseEntity.getAsByte(collection, \"$name\")"
                        }

                        typeString == "Byte" && isNullable -> {
                            "BaseEntity.getAsNullableByte(collection, \"$name\")"
                        }

                        typeString == "Short" && !isNullable -> {
                            "BaseEntity.getAsShort(collection, \"$name\")"
                        }

                        typeString == "Short" && isNullable -> {
                            "BaseEntity.getAsNullableShort(collection, \"$name\")"
                        }

                        else -> {
                            logger.error(
                                "Unsupported type: $typeString${if (isNullable) "?" else ""} for parameter $name. " +
                                    "Supported types: Int, Long, String, Char, UShort, Double, Float, Boolean, " +
                                    "Byte, Short (and nullable variants). " +
                                    "For custom types, use @FieldConverter annotation with a TypeConverter implementation.",
                                param,
                            )
                            // Return a placeholder that will cause a compile error with a clear message
                            "TODO(\"Add @FieldConverter for ${typeString}${if (isNullable) "?" else ""} or add support in BaseEntity\")"
                        }
                    }

                "            $name = $getter"
            }
    }
}
