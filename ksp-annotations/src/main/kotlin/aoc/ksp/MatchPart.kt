package aoc.ksp

/**
 * Which part of the regex match feeds a constructor parameter, for the parameters that are not read
 * from a named group.
 */
enum class MatchPart {
    /** The whole input line, verbatim. Only valid with `@GenerateStructure(customLine = true)`. */
    LINE,

    /**
     * Every match in the line, each passed to the element type's single-`String` constructor.
     * Only valid with `customLine = true`, on a `List<T>`.
     */
    ALL_MATCHES,
}
