package advent.of.code.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * KSP processor for Advent of Code solutions.
 * Generates a registry of all solutions annotated with @AdventSolution.
 */
class AdventSolutionProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("advent.of.code.common.AdventSolution")
        val validSymbols = symbols.filter { it.validate() }.toList()

        if (validSymbols.isEmpty()) {
            return emptyList()
        }

        val solutions = validSymbols.filterIsInstance<KSClassDeclaration>().map { classDecl ->
            val annotation = classDecl.annotations.first { 
                it.shortName.asString() == "AdventSolution" 
            }
            val year = annotation.arguments.first { it.name?.asString() == "year" }.value as Int
            val day = annotation.arguments.first { it.name?.asString() == "day" }.value as Int
            Triple(year, day, classDecl.qualifiedName?.asString() ?: "")
        }

        generateSolutionRegistry(solutions)

        return emptyList()
    }

    private fun generateSolutionRegistry(solutions: List<Triple<Int, Int, String>>) {
        val pairType = Pair::class.asClassName().parameterizedBy(
            Int::class.asClassName(),
            Int::class.asClassName()
        )
        val mapType = Map::class.asClassName().parameterizedBy(
            pairType,
            String::class.asClassName()
        )
        
        val fileSpec = FileSpec.builder("advent.of.code.generated", "SolutionRegistry")
            .addType(
                TypeSpec.objectBuilder("SolutionRegistry")
                    .addProperty(
                        PropertySpec.builder("solutions", mapType)
                            .initializer(
                                CodeBlock.builder()
                                    .add("mapOf(\n")
                                    .apply {
                                        solutions.forEach { (year, day, className) ->
                                            add("    Pair(%L, %L) to %S,\n", year, day, className)
                                        }
                                    }
                                    .add(")")
                                    .build()
                            )
                            .build()
                    )
                    .addFunction(
                        FunSpec.builder("getSolution")
                            .addParameter("year", Int::class)
                            .addParameter("day", Int::class)
                            .returns(String::class.asClassName().copy(nullable = true))
                            .addStatement("return solutions[Pair(year, day)]")
                            .build()
                    )
                    .build()
            )
            .build()

        fileSpec.writeTo(codeGenerator, Dependencies(false))
    }
}
