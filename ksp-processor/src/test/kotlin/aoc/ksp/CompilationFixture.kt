package aoc.ksp

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import com.tschuchort.compiletesting.useKsp2
import java.io.OutputStream

/**
 * Runs [StructureProcessor] over in-memory sources so the generated text and the diagnostics can be
 * asserted as ordinary unit tests.
 *
 * The alternative is breaking a real entity, running Gradle and reading the console, which is how
 * every diagnostic in this processor used to be checked.
 */
object CompilationFixture {
    /**
     * @param sources Kotlin sources to feed the processor; use [entity] for the common case
     * @return what the processor produced and said
     */
    fun process(vararg sources: SourceFile): ProcessorResult {
        val compilation =
            KotlinCompilation().apply {
                this.sources = sources.toList()
                inheritClassPath = true
                useKsp2()
                symbolProcessorProviders.add(StructureProcessorProvider())
                messageOutputStream = OutputStream.nullOutputStream()
            }

        val result = compilation.compile()

        val generated =
            compilation.kspSourcesDir
                .walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .associate { it.name to it.readText() }

        return ProcessorResult(result.exitCode, result.messages, generated)
    }

    /** A source file holding a single entity, with the `aoc.ksp` imports the processor needs. */
    fun entity(
        name: String,
        body: String,
        imports: List<String> = emptyList(),
    ): SourceFile =
        SourceFile.kotlin(
            "$name.kt",
            buildString {
                appendLine("package test")
                appendLine()
                imports.forEach { appendLine("import $it") }
                appendLine()
                append(body)
            },
        )

    data class ProcessorResult(
        val exitCode: KotlinCompilation.ExitCode,
        val messages: String,
        val generated: Map<String, String>,
    ) {
        /** The generated companion for [entityName], or null when the processor produced none. */
        fun companionFor(entityName: String): String? = generated["${entityName}Companion.kt"]

        val succeeded: Boolean get() = exitCode == KotlinCompilation.ExitCode.OK
    }
}
