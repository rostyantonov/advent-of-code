package aoc.year2015.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Connection(
    val from: String,
    val to: String,
    val value: Int,
) {
    val fromTo: Pair<String, String>
        get() = Pair(from, to)
}
