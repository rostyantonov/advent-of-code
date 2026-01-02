package aoc.year2015.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Attendee(
    val who: String,
    val to: String,
    val state: GainLose,
    val rawAmount: Int,
) {
    val whoTo: Pair<String, String>
        get() = Pair(who, to)

    val amount: Int
        get() =
            when (state) {
                GainLose.GAIN -> rawAmount
                GainLose.LOSE -> -rawAmount
            }
}
