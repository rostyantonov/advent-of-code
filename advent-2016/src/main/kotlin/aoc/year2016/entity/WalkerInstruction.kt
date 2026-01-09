package aoc.year2016.entity

import aoc.ksp.BaseEntity.getAsChar
import aoc.ksp.BaseEntity.getAsInt
import aoc.ksp.IStructureLine

data class WalkerInstruction(
    val direction: Char,
    val steps: Int,
) {
    companion object : IStructureLine<WalkerInstruction> {
        override fun create(collection: MatchResult): WalkerInstruction =
            WalkerInstruction(
                direction = getAsChar(collection.groups, "direction"),
                steps = getAsInt(collection.groups, "steps"),
            )
    }
}