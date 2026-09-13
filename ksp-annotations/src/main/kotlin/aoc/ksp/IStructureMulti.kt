package aoc.ksp

interface IStructureMulti<Type : Any> : IStructureSkips {
    fun fromLine(
        line: String,
        regexArray: Array<Regex>,
    ): Type =
        regexArray
            .firstNotNullOfOrNull { regex -> regex.matchEntire(line) }
            ?.let { match -> create(match.groups) }
            ?: throw IllegalArgumentException("No regex in regexArray matched line: '$line'")

    fun create(collection: MatchGroupCollection): Type
}
