package aoc.common.entity

import kotlin.math.abs

data class Position(
    val xPos: Int,
    val yPos: Int,
) {
    private val up: Int
        get() = yPos - 1
    private val down: Int
        get() = yPos + 1
    private val left: Int
        get() = xPos - 1
    private val right: Int
        get() = xPos + 1

    operator fun minus(other: Position): Position = Position(this.xPos - other.xPos, this.yPos - other.yPos)

    operator fun plus(other: Position): Position = Position(this.xPos + other.xPos, this.yPos + other.yPos)

    fun getUp() = Position(xPos, up)

    fun getDown() = Position(xPos, down)

    fun getLeft() = Position(left, yPos)

    fun getRight() = Position(right, yPos)

    fun getUpLeft() = Position(left, up)

    fun getUpRight() = Position(right, up)

    fun getDownLeft() = Position(left, down)

    fun getDownRight() = Position(right, down)

    /**
     * Calculates Manhattan distance between this position and another position.
     * Manhattan distance is the sum of absolute differences of coordinates.
     * @param other The target position
     * @return The Manhattan distance as an integer
     */
    fun manhattanDistance(other: Position): Int = abs(xPos - other.xPos) + abs(yPos - other.yPos)

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
