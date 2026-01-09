package advent.of.code.ksp

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Provider for the AdventSolutionProcessor.
 * This is required by KSP to instantiate the processor.
 */
class AdventSolutionProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return AdventSolutionProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}
