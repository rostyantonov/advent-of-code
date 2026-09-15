package aoc.common.input

import aoc.ksp.IStructure
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
 *
 * Example usage:
 * ```kotlin
 * val inputFunction = StructuredInput.of(
 *     regex = Regex("""(?<name>\w+): (?<value>\d+)"""),
 *     structure = MyEntityCompanion,
 * )::getStructInput
 * ```
 *
 * Prefer the [of] factories: they read the skip counts off the generated companion. The constructor
 * is for trimming that belongs to one puzzle rather than to the entity.
 */
class StructuredInput<Structure>(
    private val regex: Regex?,
    private val builder: KFunction2<String, Regex?, Structure>,
) {
    /**
     * Parse input lines into a list of structured entities.
     *
     * @param blockInput All input lines
     * @return List of parsed entities
     */
    fun getStructInput(blockInput: List<String>): List<Structure> =
        blockInput.map { string ->
            builder(string, regex)
        }

    /**
     * Parse input lines and return a single structured entity (first matching line).
     *
     * @param blockInput All input lines
     * @return First parsed entity
     */
    fun getSingleStructInput(blockInput: List<String>): Structure = builder(blockInput.first(), regex)

    /**
     * Factory that take the generated companion itself, so the skip counts declared on the entity
     * through `@GenerateStructure` travel with it instead of being repeated at every call site.
     */
    companion object {
        @JvmName("ofStructure")
        fun <Type> of(
            regex: Regex?,
            structure: IStructure<Type>,
        ): StructuredInput<Type> =
            StructuredInput(
                regex,
                structure::fromLine,
            )
    }
}
