package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed class Card {
    data class Rect(
        val xPos: Int,
        val yPos: Int,
    ) : Card()

    data class Row(
        val row: Int,
        val amount: Int,
    ) : Card()

    data class Column(
        val column: Int,
        val amount: Int,
    ) : Card()
}
