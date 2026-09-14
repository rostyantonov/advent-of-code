package aoc.common.input

import aoc.common.input.StringInput.filterLines
import aoc.ksp.IStructure
import aoc.ksp.IStructureCustomLine
import aoc.ksp.IStructureEnum
import aoc.ksp.IStructureLine
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
 * @property splitBy Optional separator that splits one line into several entities (default: null,
 *                   meaning one entity per line). Useful for inputs like "se,ne,se,n"
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
    private val skipHeaderLines: Int = 0,
    private val skipFooterLines: Int = 0,
    private val splitBy: String? = null,
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
        filterLines(blockInput, skipHeaderLines, skipFooterLines).map { string ->
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
     * Parse a single-line input that packs several entities into one line, separated by [splitBy].
     *
     * @param blockInput All input lines; only the first one is read
     * @return List of parsed entities
     */
    fun getFirstLineStructInput(blockInput: List<String>): List<Structure> {
        val separator = requireNotNull(splitBy) { "getFirstLineStructInput needs a splitBy separator" }
        return getStructInput(blockInput.first().split(separator))
    }

    /**
     * Factories that take the generated companion itself, so the skip counts declared on the entity
     * through `@GenerateStructure` travel with it instead of being repeated at every call site.
     *
     * They are factories rather than constructors because `IStructure<T>`, `IStructureLine<T>` and
     * `IStructureCustomLine<T>` all erase to the same JVM signature; the `@JvmName`s keep them apart
     * while leaving one name to call in Kotlin.
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
                structure.skipHeaderLines,
                structure.skipFooterLines,
            )

        @JvmName("ofStructureLine")
        fun <Type> of(
            regex: Regex?,
            structure: IStructureLine<Type>,
        ): StructuredInput<List<Type>> =
            StructuredInput(
                regex,
                structure::fromLine,
                structure.skipHeaderLines,
                structure.skipFooterLines,
            )

        @JvmName("ofStructureCustomLine")
        fun <Type> of(
            regex: Regex?,
            structure: IStructureCustomLine<Type>,
        ): StructuredInput<Type> =
            StructuredInput(
                regex,
                structure::fromLine,
                structure.skipHeaderLines,
                structure.skipFooterLines,
            )

        /**
         * An enum entity is the one mode where the regex is optional - the whole token is the
         * constant - and the one that so far needs [splitBy], because such tokens tend to arrive
         * packed into a single line.
         */
        @JvmName("ofStructureEnum")
        fun <Type : Enum<Type>> of(
            regex: Regex? = null,
            structure: IStructureEnum<Type>,
            splitBy: String? = null,
        ): StructuredInput<Type> =
            StructuredInput(
                regex,
                structure::fromLine,
                structure.skipHeaderLines,
                structure.skipFooterLines,
                splitBy,
            )
    }
}
