package aoc.ksp

/**
 * Companion contract for an entity that *is* an enum constant rather than one carrying enum fields.
 *
 * The regex is optional here, unlike in the other four interfaces: when the whole input token is the
 * constant there is nothing to name a group after. Pass one only to narrow what counts as a token.
 */
interface IStructureEnum<Type : Enum<Type>> : IStructureSkips {
    fun fromLine(
        line: String,
        regex: Regex? = null,
    ): Type {
        val token =
            regex?.let {
                it.matchEntire(line)?.value
                    ?: throw IllegalArgumentException("Regex /${it.pattern}/ did not match line: '$line'")
            } ?: line

        return create(token)
    }

    fun create(token: String): Type
}
