package aoc.ksp

interface IStructureCustomLine<Type> {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): Type {
        requireNotNull(regex) { "${this::class.simpleName} needs a regex to parse line: '$line'" }
        return create(line, regex.findAll(line))
    }

    fun create(
        line: String,
        collection: Sequence<MatchResult>,
    ): Type
}
