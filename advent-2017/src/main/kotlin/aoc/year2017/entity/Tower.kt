package aoc.year2017.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Tower(
    val name: String,
    val weight: Int,
    val items: String?,
) {
    val itemNames: List<String>?
        get() = items?.split(", ")

    var calculatedWeight: Int = 0
}
