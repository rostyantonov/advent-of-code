package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Triangle(
    val aSide: Int,
    val bSide: Int,
    val cSide: Int,
) {
    fun isPossible(): Boolean = aSide + bSide > cSide && bSide + cSide > aSide && aSide + cSide > bSide
}
