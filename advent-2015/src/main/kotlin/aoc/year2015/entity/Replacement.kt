package aoc.year2015.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Replacement(
    val what: String,
    val replace: String,
)
