package aoc.common.entity

object BaseEntity {
    fun getAsInt(
        collection: MatchGroupCollection,
        name: String,
    ) = getAsNullableInt(collection, name)!!

    fun getAsNullableInt(
        collection: MatchGroupCollection,
        name: String,
    ) = collection[name]?.value?.toIntOrNull()
}
