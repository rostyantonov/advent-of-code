package aoc.ksp

interface IStructureLine<Type> {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): List<Type> = regex!!.findAll(line).map { create(it) }.toList()

    fun create(collection: MatchResult): Type
}
