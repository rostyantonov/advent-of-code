package aoc.year2016.entity

import aoc.common.entity.Position

/**
 * Represents a state in the vault navigation puzzle.
 *
 * @property position Current position in the 4x4 grid
 * @property path Path taken so far (sequence of U/D/L/R moves)
 */
data class RoomState(
    val position: Position,
    val path: String,
)
