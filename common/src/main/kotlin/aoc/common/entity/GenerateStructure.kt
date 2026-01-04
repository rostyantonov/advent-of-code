package aoc.common.entity

/**
 * Annotation to mark data classes that should have their IStructure companion object generated.
 *
 * The KSP processor will generate a companion object implementing IStructure<T> or IStructureCustomLine<T> that:
 * - Extracts field values from regex named groups matching the field names (when customLine=false)
 * - Processes the entire line and match sequence (when customLine=true)
 * - Supports Int, Long, String, Char, UShort (and nullable variants)
 *
 * @param customLine If true, generates IStructureCustomLine<T> with create(line, collection: Sequence<MatchResult>)
 *                   If false (default), generates IStructure<T> with create(collection: MatchGroupCollection)
 *
 * Example (standard):
 * ```
 * @GenerateStructure
 * data class Present(
 *     val length: Int,
 *     val width: Int,
 *     val height: Int,
 * )
 * ```
 *
 * Example (custom line):
 * ```
 * @GenerateStructure(customLine = true)
 * data class Molecule(
 *     val stringValue: String,
 *     val atoms: List<Atom>,
 * )
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateStructure(
    val customLine: Boolean = false,
)
