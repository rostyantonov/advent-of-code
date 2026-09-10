package aoc.common.entity

import kotlin.math.abs

data class HexPosition(
    var xPos: Int,
    var yPos: Int,
    var zPos: Int,
) {
    fun getNorth(): HexPosition = HexPosition(xPos, yPos + 1, zPos - 1)

    fun getSouth(): HexPosition = HexPosition(xPos, yPos - 1, zPos + 1)

    fun getNorthEast(): HexPosition = HexPosition(xPos + 1, yPos, zPos - 1)

    fun getSouthWest(): HexPosition = HexPosition(xPos - 1, yPos, zPos + 1)

    fun getNorthWest(): HexPosition = HexPosition(xPos - 1, yPos + 1, zPos)

    fun getSouthEast(): HexPosition = HexPosition(xPos + 1, yPos - 1, zPos)

    fun manhattanDistance(other: HexPosition): Int =
        maxOf(
            abs(xPos - other.xPos),
            abs(yPos - other.yPos),
            abs(zPos - other.zPos),
        )
}
