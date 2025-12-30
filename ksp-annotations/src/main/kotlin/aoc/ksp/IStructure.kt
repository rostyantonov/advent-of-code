package aoc.ksp

interface IStructure<Type> {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): Type {
        requireNotNull(regex) { "${this::class.simpleName} needs a regex to parse line: '$line'" }
        val match =
            regex.matchEntire(line)
                ?: throw IllegalArgumentException("Regex /${regex.pattern}/ did not match line: '$line'")
        return create(match.groups)
    }

    fun create(collection: MatchGroupCollection): Type
}
