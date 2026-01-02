package aoc.year2015.entity

import aoc.common.entity.BaseEntity.getAsInt
import aoc.common.entity.BaseEntity.getAsString
import aoc.common.entity.IStructure
import aoc.common.util.safeValue

data class Attendee(
    val whoTo: Pair<String, String>,
    val amount: Int,
) {
    companion object : IStructure<Attendee> {
        override fun create(collection: MatchGroupCollection): Attendee =
            Attendee(
                whoTo =
                    Pair(
                        getAsString(collection, "who"),
                        getAsString(collection, "to"),
                    ),
                amount =
                    when (safeValue<GainLose>(getAsString(collection, "state"))) {
                        GainLose.GAIN -> getAsInt(collection, "amount")
                        GainLose.LOSE -> 0 - getAsInt(collection, "amount")
                    },
            )
    }
}
