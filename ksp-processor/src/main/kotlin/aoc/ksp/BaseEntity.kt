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

    fun getAsLong(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableLong(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Long) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableLong(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toLongOrNull()

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

    fun getAsUShort(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableUShort(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected UShort) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableUShort(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toUShortOrNull()

    fun getAsDouble(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableDouble(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Double) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableDouble(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toDoubleOrNull()

    fun getAsFloat(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableFloat(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Float) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableFloat(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toFloatOrNull()

    fun getAsBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableBoolean(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Boolean) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableBoolean(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.let { 
        when (it.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }

    fun getAsByte(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableByte(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Byte) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableByte(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toByteOrNull()

    fun getAsShort(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableShort(collection, name)
        ?: throw IllegalArgumentException(
            "Field '$name' (expected Short) not found or invalid in regex groups. " +
                "Ensure regex has named group (?<$name>...)",
        )

    fun getAsNullableShort(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toShortOrNull()
}
