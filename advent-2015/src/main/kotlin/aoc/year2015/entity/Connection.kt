package aoc.year2015.entity

import aoc.common.entity.BaseEntity.getAsInt
import aoc.common.entity.BaseEntity.getAsString
import aoc.common.entity.IStructure

data class Connection(
    val fromTo: Pair<String, String>,
    val `value`: Int,
) {
    companion object : IStructure<Connection> {
        override fun create(collection: MatchGroupCollection): Connection =
            Connection(
                fromTo =
                    Pair(
                        getAsString(collection, "from"),
                        getAsString(collection, "to"),
                    ),
                value = getAsInt(collection, "value"),
            )
    }
}
