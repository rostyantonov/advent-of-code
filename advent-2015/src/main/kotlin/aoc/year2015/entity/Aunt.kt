package aoc.year2015.entity

import aoc.common.entity.BaseEntity.getAsInt
import aoc.common.entity.BaseEntity.getAsString
import aoc.common.entity.IStructure

data class Aunt(
    val index: Int,
    val param: Map<String, Int>,
) {
    companion object : IStructure<Aunt> {
        override fun create(collection: MatchGroupCollection): Aunt =
            Aunt(
                index = getAsInt(collection, "index"),
                param =
                    mutableMapOf(
                        Pair(getAsString(collection, "param1name"), getAsInt(collection, "param1val")),
                        Pair(getAsString(collection, "param2name"), getAsInt(collection, "param2val")),
                        Pair(getAsString(collection, "param3name"), getAsInt(collection, "param3val")),
                    ),
            )
    }
}
