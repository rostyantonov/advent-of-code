package aoc.ksp

interface IStructureLine<Type> : IStructureSkips {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): List<Type> {
        requireNotNull(regex) { "${this::class.simpleName} needs a regex to parse line: '$line'" }
        // No match is a valid answer here: callers such as 2016 Day09 ask "how many markers does this
        // line carry?" and an unmarked line legitimately carries none.
        return regex.findAll(line).map { create(it) }.toList()
    }

    fun create(collection: MatchResult): Type
}
