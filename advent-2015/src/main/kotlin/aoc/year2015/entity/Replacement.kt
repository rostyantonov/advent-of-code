package aoc.year2015.entity

import aoc.common.entity.GenerateStructure

@GenerateStructure
data class Replacement(
    val what: String,
    val replace: String,
)
