package aoc.ksp

/**
 * Which part of the regex match feeds a constructor parameter, for the parameters that are not read
 * from a named group.
 */
enum class MatchPart {
    /** The whole input line, verbatim. Only valid with `@GenerateStructure(customLine = true)`. */
    LINE,

    /** The match's own span within the line. Only valid with `lineBased = true`, on an `IntRange`. */
    RANGE,

    /**
     * Every match in the line, each passed to the element type's single-`String` constructor.
     * Only valid with `customLine = true`, on a `List<T>`.
     */
    ALL_MATCHES,
}

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
