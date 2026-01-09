package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(lineBased = true)
data class WalkerInstruction(
    val direction: Char,
    val steps: Int,
)
