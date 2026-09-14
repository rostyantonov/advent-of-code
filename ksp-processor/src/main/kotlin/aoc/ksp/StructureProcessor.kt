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
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate

class StructureProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    private val mappings = ParameterMappings(logger)
    private val templates = CompanionTemplates(mappings)

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

            // A hand-written companion is a deliberate choice; generating a second one would not
            // compile, so the entity is left alone.
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

            val options = StructureOptions.from(classDeclaration, logger) ?: return

            val parameters = constructorParameters(classDeclaration, options) ?: return

            // @FromMatch describes sources only the customLine and lineBased templates can read; the
            // standard and multi templates would silently ignore it.
            if (!options.customLine && !options.lineBased) {
                parameters.filter { mappings.matchPartOf(it) != null }.forEach { param ->
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
                if (options.multiStructure) {
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
            if (options.multiStructure && sealedSubclasses.isEmpty()) return

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

            val skips = templates.skipOverrides(options.skipHeaderLines, options.skipFooterLines)

            val companionSource =
                when {
                    options.multiStructure ->
                        templates.multiStructure(
                            packageName,
                            className,
                            sealedSubclasses,
                            options.discriminatorField,
                            skips,
                        )

                    options.customLine -> templates.customLine(packageName, className, parameters, skips)

                    options.lineBased -> templates.lineBased(packageName, className, parameters, skips)

                    else -> templates.standard(packageName, className, parameters, skips)
                }

            file.bufferedWriter().use { writer -> writer.write(companionSource) }

            logger.info("Generated ${options.companionInterface} companion for $packageName.$className")
        }

        /**
         * The parameters the generated `create` has to fill, or null when the class cannot supply
         * any. A multi-structure hierarchy reads its parameters off the subclasses instead, so the
         * sealed parent itself is allowed to have no constructor at all.
         */
        private fun constructorParameters(
            classDeclaration: KSClassDeclaration,
            options: StructureOptions,
        ): List<KSValueParameter>? {
            if (options.multiStructure) return emptyList()

            val constructor = classDeclaration.primaryConstructor
            if (constructor == null) {
                logger.error("@GenerateStructure class must have a primary constructor", classDeclaration)
                return null
            }

            if (constructor.parameters.isEmpty()) {
                logger.error("@GenerateStructure class must have at least one parameter", classDeclaration)
                return null
            }

            return constructor.parameters
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
