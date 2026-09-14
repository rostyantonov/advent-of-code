package aoc.ksp

/**
 * Optional annotation that overrides the token a declaration is matched by, so a readable Kotlin
 * name can answer to the short spelling the puzzle input actually carries.
 *
 * It reads in two places:
 * - on a subclass of a `@GenerateStructure(multiStructure = true)` sealed class, where it replaces
 *   the subclass simple name as the discriminator token;
 * - on an enum constant, where it replaces the constant name as the token [BaseEntity] matches,
 *   whether the enum is a field of an entity or the entity itself.
 *
 * Matching is case-insensitive in both places: `@StructureName("s")` and `@StructureName("S")`
 * behave identically. The alias *replaces* the declared name rather than joining it, so an aliased
 * subclass or constant no longer answers to what it is called in Kotlin.
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
 *
 * enum class Turn {
 *     @StructureName("L")
 *     Left,
 *
 *     @StructureName("R")
 *     Right,
 * }
 * // Input "L4" -> Turn.Left
 * ```
 *
 * @param value The token that routes to the annotated subclass or constant
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class StructureName(
    val value: String,
)
