package aoc.common.input

import aoc.ksp.IStructureMulti
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
 *
 * Example usage:
 * ```kotlin
 * val inputFunction = StructuredMultiInput.of(
 *     regexArray = arrayOf(
 *         Regex("""pattern1"""),
 *         Regex("""pattern2""")
 *     ),
 *     structure = MySealedClassCompanion,
 * )::getStructInput
 * ```
 *
 * Prefer the [of] factory: it reads the skip counts off the generated companion. The constructor is
 * for trimming that belongs to one puzzle rather than to the entity.
 */
class StructuredMultiInput<Structure>(
    private val regexArray: Array<Regex>,
    private val builder: KFunction2<String, Array<Regex>, Structure>,
) {
    /**
     * Parse input lines into a list of structured entities.
     *
     * Lines are automatically filtered to skip headers and footers as configured.
     *
     * @param blockInput All input lines
     * @return List of parsed entities
     */
    fun getStructInput(blockInput: List<String>): List<Structure> =
        blockInput.map { string ->
            builder(string, regexArray)
        }

    companion object {
        /**
         * Takes the generated companion itself, so the skip counts declared on the entity through
         * `@GenerateStructure` travel with it instead of being repeated at every call site.
         */
        fun <Type : Any> of(
            regexArray: Array<Regex>,
            structure: IStructureMulti<Type>,
        ): StructuredMultiInput<Type> =
            StructuredMultiInput(
                regexArray,
                structure::fromLine,
            )
    }
}
