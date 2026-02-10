package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(lineBased = true)
data class Decompress(
    val range: IntRange,
    val num: Int,
    val times: Int,
)
