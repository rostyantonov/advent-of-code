package aoc.year2015.entity

import aoc.common.entity.GenerateStructure

@GenerateStructure
data class Ingredient(
    val name: String,
    val capacity: Int,
    val durability: Int,
    val flavor: Int,
    val texture: Int,
    val calories: Int,
)
