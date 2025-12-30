package aoc.common.entity

interface IStructure<Type> {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): Type = create(regex!!.matchEntire(line)!!.groups)

    fun create(collection: MatchGroupCollection): Type
}
