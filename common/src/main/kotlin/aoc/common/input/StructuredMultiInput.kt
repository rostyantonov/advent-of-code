package aoc.common.input

import kotlin.reflect.KFunction2

/**
 * Helper class for parsing structured input with multiple regex patterns.
 *
 * This class works with sealed class entities annotated with @GenerateStructure(multiStructure=true),
 * using multiple regex patterns to match different entity types.
 *
 * @param Structure The sealed class type being parsed
 * @property regexArray Array of regex patterns to try matching
 * @property builder The companion object's fromLine function (KSP-generated)
 * @property skipHeaderLines Number of lines to skip at the beginning (default: 0)
 * @property skipFooterLines Number of lines to skip at the end (default: 0)
 *
 * Example usage:
 * ```kotlin
 * val inputFunction = StructuredMultiInput(
 *     regexArray = arrayOf(
 *         Regex("""pattern1"""),
 *         Regex("""pattern2""")
 *     ),
 *     builder = MySealedClassCompanion::fromLine,
 *     skipHeaderLines = 1
 * )::getStructInput
 * ```
 */
class StructuredMultiInput<Structure>(
    private val regexArray: Array<Regex>,
    private val builder: KFunction2<String, Array<Regex>, Structure>,
    private val skipHeaderLines: Int = 0,
    private val skipFooterLines: Int = 0,
) {
    /**
     * Parse input lines into a list of structured entities.
     *
     * Lines are automatically filtered to skip headers and footers as configured.
     *
     * @param blockInput All input lines
     * @return List of parsed entities
     */
    fun getStructInput(blockInput: List<String>): List<Structure> {
        val filteredLines =
            when {
                skipHeaderLines > 0 && skipFooterLines > 0 -> {
                    blockInput.drop(skipHeaderLines).dropLast(skipFooterLines)
                }

                skipHeaderLines > 0 -> {
                    blockInput.drop(skipHeaderLines)
                }

                skipFooterLines > 0 -> {
                    blockInput.dropLast(skipFooterLines)
                }

                else -> {
                    blockInput
                }
            }
        return filteredLines.map { string ->
            builder(string, regexArray)
        }
    }
}
