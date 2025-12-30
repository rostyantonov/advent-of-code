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
}
