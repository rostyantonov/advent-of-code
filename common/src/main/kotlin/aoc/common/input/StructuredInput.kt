package aoc.common.input

import kotlin.reflect.KFunction2

/**
 * Helper class for parsing structured input from regex patterns.
 *
 * This class works with entities annotated with @GenerateStructure,
 * extracting field values from regex named groups.
 *
 * @param Structure The type of data class being parsed
 * @property regex The regex pattern with named groups matching entity fields
 * @property builder The companion object's fromLine function (KSP-generated)
 * @property skipHeaderLines Number of lines to skip at the beginning (default: 0)
 * @property skipFooterLines Number of lines to skip at the end (default: 0)
 *
 * Example usage:
 * ```kotlin
 * val inputFunction = StructuredInput(
 *     regex = Regex("""(?<name>\w+): (?<value>\d+)"""),
 *     builder = MyEntityCompanion::fromLine,
 *     skipHeaderLines = 2  // Skip 2 header lines
 * )::getStructInput
 * ```
 */
class StructuredInput<Structure>(
    private val regex: Regex?,
    private val builder: KFunction2<String, Regex?, Structure>,
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
            builder(string, regex)
        }
    }

    /**
     * Parse input lines and return a single structured entity (first matching line).
     *
     * @param blockInput All input lines
     * @return First parsed entity
     */
    fun getSingleStructInput(blockInput: List<String>): Structure =
        blockInput
            .map { string ->
                builder(string, regex)
            }.first()
}
