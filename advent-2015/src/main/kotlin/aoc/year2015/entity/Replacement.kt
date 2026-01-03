package aoc.year2015.entity

import aoc.common.entity.BaseEntity.getAsString
import aoc.common.entity.IStructure

data class Replacement(
    val what: String,
    val replace: String,
) {
    companion object : IStructure<Replacement> {
        override fun create(collection: MatchGroupCollection): Replacement =
            Replacement(
                what = getAsString(collection, "what"),
                replace = getAsString(collection, "replace"),
            )
    }
}
