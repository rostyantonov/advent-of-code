package aoc.year2016.entity

import aoc.ksp.FromMatch
import aoc.ksp.GenerateStructure
import aoc.ksp.MatchPart

@GenerateStructure(lineBased = true)
data class Decompress(
    @FromMatch(MatchPart.RANGE) val range: IntRange,
    val num: Int,
    // Long because part two multiplies the repetition counts together, which overflows an Int
    val times: Long,
)
