package aoc.ksp

object BaseEntity {
    fun getAsInt(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableInt(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Int) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableInt(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toIntOrNull()

    fun getAsString(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableString(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected String) not found in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableString(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value

    fun getAsChar(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableChar(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Char) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableChar(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.firstOrNull()
}
