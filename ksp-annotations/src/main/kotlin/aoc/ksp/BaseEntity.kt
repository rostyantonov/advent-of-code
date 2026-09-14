package aoc.ksp

/**
 * Regex-group accessors used by the code the KSP StructureProcessor generates.
 *
 * Every supported type has a nullable getter (returns null when the group is absent or unparseable)
 * and a non-nullable getter that wraps it and fails loudly. The processor picks between them based
 * on whether the constructor parameter is marked nullable.
 */
object BaseEntity {
    private fun <T> required(
        name: String,
        type: String,
        value: T?,
    ): T =
        value ?: throw IllegalArgumentException(
            "Field '$name' (expected $type) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsString(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "String", getAsNullableString(collection, name))

    fun getAsNullableString(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value

    fun getAsChar(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Char", getAsNullableChar(collection, name))

    fun getAsNullableChar(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.firstOrNull()

    fun getAsInt(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Int", getAsNullableInt(collection, name))

    fun getAsNullableInt(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toIntOrNull()

    fun getAsLong(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Long", getAsNullableLong(collection, name))

    fun getAsNullableLong(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toLongOrNull()

    fun getAsBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = required(name, "Boolean", getAsNullableBoolean(collection, name))

    /**
     * Note this is stricter than [String.toBoolean], which maps anything that is not "true" to
     * false: a group that matched something else is a regex mistake, not a false.
     */
    fun getAsNullableBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toBooleanStrictOrNull()

    inline fun <reified T : Enum<T>> getAsEnum(
        collection: MatchGroupCollection,
        name: String,
    ): T =
        getAsNullableEnum<T>(collection, name)
            ?: throw IllegalArgumentException(
                "Field '$name' (expected ${T::class.simpleName}) not found or invalid in regex groups. " +
                    "Ensure regex has named group (?<$name>...) and that it matches one of " +
                    enumValues<T>().joinToString(", ") { it.name },
            )

    /** The group value read as a [T] constant; see [asNullableEnum] for how the text is matched. */
    inline fun <reified T : Enum<T>> getAsNullableEnum(
        collection: MatchGroupCollection,
        name: String,
    ): T? = asNullableEnum<T>(collection[name]?.value ?: return null)

    /**
     * [raw] read as a [T] constant, for the enum entities whose whole token is the constant and so
     * have no named group to read.
     */
    inline fun <reified T : Enum<T>> asEnum(raw: String): T =
        asNullableEnum<T>(raw)
            ?: throw IllegalArgumentException(
                "'$raw' is not a ${T::class.simpleName}; expected one of " +
                    enumValues<T>().joinToString(", ") { it.name },
            )

    /**
     * [raw] read as a [T] constant, normalised the way input text tends to differ from Kotlin
     * naming: case is ignored and spaces stand in for underscores ("turn on" -> TURN_ON).
     *
     * The single normalisation rule for both entry points, so a field enum and an entity enum cannot
     * disagree about what a token means.
     */
    inline fun <reified T : Enum<T>> asNullableEnum(raw: String): T? {
        val normalised = raw.trim().replace(' ', '_').uppercase()
        return enumValues<T>().firstOrNull { it.name == normalised }
    }
}
