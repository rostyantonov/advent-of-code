package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Disc(
    val depth: Int,
    val positions: Int,
    val start: Int,
) {
    fun isOpenAt(time: Int): Boolean = (start + time + depth) % positions == 0
}
