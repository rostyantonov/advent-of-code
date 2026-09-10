package aoc.ksp

/**
 * Optional annotation that overrides the discriminator token used to select a sealed subclass
 * in a `@GenerateStructure(multiStructure = true)` companion.
 *
 * Without it the subclass simple name is used, so existing entities are unaffected.
 * Matching is case-insensitive: `@StructureName("s")` and `@StructureName("S")` behave identically.
 *
 * Example:
 * ```
 * @GenerateStructure(multiStructure = true, discriminatorField = "type")
 * sealed class DanceMove {
 *     @StructureName("s")
 *     data class Spin(val steps: Int) : DanceMove()
 *
 *     @StructureName("x")
 *     data class Exchange(val from: Int, val to: Int) : DanceMove()
 * }
 * // Input "s3" -> DanceMove.Spin(3), "x3/4" -> DanceMove.Exchange(3, 4)
 * ```
 *
 * @param value The discriminator token that routes to the annotated subclass
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class StructureName(
    val value: String,
)
