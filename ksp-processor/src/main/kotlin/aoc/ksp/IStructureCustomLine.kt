package aoc.ksp

interface IStructureCustomLine<Type> {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): Type = create(line, regex!!.findAll(line))

    fun create(
        line: String,
        collection: Sequence<MatchResult>,
    ): Type
}
