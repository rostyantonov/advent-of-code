package aoc.year2015.entity

import aoc.ksp.GenerateStructure
import aoc.common.util.safeValue

@GenerateStructure
data class Attendee(
    val who: String,
    val to: String,
    val state: String,
    val rawAmount: Int,
) {
    val whoTo: Pair<String, String>
        get() = Pair(who, to)

    val amount: Int
        get() =
            when (safeValue<GainLose>(state)) {
                GainLose.GAIN -> rawAmount
                GainLose.LOSE -> -rawAmount
            }
}
