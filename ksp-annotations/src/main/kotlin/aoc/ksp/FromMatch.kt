package aoc.ksp

/**
 * Marks a constructor parameter as coming from [part] of the match rather than from a named group.
 *
 * Without this annotation the processor looks for a regex group sharing the parameter's name, so a
 * parameter that has no corresponding group needs one of the [MatchPart] sources spelled out here.
 *
 * Example:
 * ```
 * @GenerateStructure(customLine = true)
 * data class Molecule(
 *     @FromMatch(MatchPart.LINE) val stringValue: String,
 *     @FromMatch(MatchPart.ALL_MATCHES) val atoms: List<Atom>,
 * )
 * ```
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class FromMatch(
    val part: MatchPart,
)
