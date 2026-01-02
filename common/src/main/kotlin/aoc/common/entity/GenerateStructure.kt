package aoc.common.entity

/**
 * Annotation to mark data classes that should have their IStructure companion object generated.
 *
 * The KSP processor will generate a companion object implementing IStructure<T> that:
 * - Extracts field values from regex named groups matching the field names
 * - Supports Int, Long, String, Char, UShort (and nullable variants)
 *
 * Example:
 * ```
 * @GenerateStructure
 * data class Present(
 *     val length: Int,
 *     val width: Int,
 *     val height: Int,
 * ) {
 *     companion object
 * }
 * ```
 *
 * This will generate:
 * ```
 * object PresentCompanion : IStructure<Present> {
 *     override fun create(collection: MatchGroupCollection): Present =
 *         Present(
 *             length = BaseEntity.getAsInt(collection, "length"),
 *             width = BaseEntity.getAsInt(collection, "width"),
 *             height = BaseEntity.getAsInt(collection, "height"),
 *         )
 * }
 *
 * fun Present.Companion.fromRegex(line: String, regex: Regex): Present =
 *     PresentCompanion.fromLine(line, regex)
 * ```
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateStructure
