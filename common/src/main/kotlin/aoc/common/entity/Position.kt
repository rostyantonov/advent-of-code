package aoc.common.entity

import kotlin.math.abs

data class Position(
    val row: Int,
    val col: Int,
) {
    private val up: Int
        get() = row - 1
    private val down: Int
        get() = row + 1
    private val left: Int
        get() = col - 1
    private val right: Int
        get() = col + 1

    operator fun minus(other: Position): Position = Position(this.row - other.row, this.col - other.col)

    operator fun plus(other: Position): Position = Position(this.row + other.row, this.col + other.col)

    fun getUp() = Position(up, col)

    fun getDown() = Position(down, col)

    fun getLeft() = Position(row, left)

    fun getRight() = Position(row, right)

    fun getUpLeft() = Position(up, left)

    fun getUpRight() = Position(up, right)

    fun getDownLeft() = Position(down, left)

    fun getDownRight() = Position(down, right)

    /**
     * Calculates Manhattan distance between this position and another position.
     * Manhattan distance is the sum of absolute differences of coordinates.
     * @param other The target position
     * @return The Manhattan distance as an integer
     */
    fun manhattanDistance(other: Position): Int = abs(row - other.row) + abs(col - other.col)

    /**
     * Returns a list of all 4 adjacent positions (up, down, left, right).
     * @return List of 4 orthogonally adjacent positions
     */
    fun adjacentPositions(): List<Position> =
        listOf(
            getUp(),
            getDown(),
            getLeft(),
            getRight(),
        )

    /**
     * Returns a list of all 8 neighboring positions (including diagonals).
     * @return List of 8 neighboring positions
     */
    fun neighbors(): List<Position> =
        listOf(
            getUp(),
            getDown(),
            getLeft(),
            getRight(),
            getUpLeft(),
            getUpRight(),
            getDownLeft(),
            getDownRight(),
        )
}
