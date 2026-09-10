package aoc.common.entity.walker

import aoc.common.entity.HexPosition

class HexWalker(
    var hexPosition: HexPosition = HexPosition(0, 0, 0),
) {
    private val fullPath: MutableList<HexPosition> = mutableListOf(hexPosition)

    val visitedPoints: Set<HexPosition>
        get() = fullPath.toSet()

    fun moveNorth(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getNorth()
            fullPath.add(hexPosition)
        }
    }

    fun moveSouth(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getSouth()
            fullPath.add(hexPosition)
        }
    }

    fun moveNorthEast(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getNorthEast()
            fullPath.add(hexPosition)
        }
    }

    fun moveSouthWest(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getSouthWest()
            fullPath.add(hexPosition)
        }
    }

    fun moveNorthWest(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getNorthWest()
            fullPath.add(hexPosition)
        }
    }

    fun moveSouthEast(steps: Int = 1) {
        repeat(steps) {
            hexPosition = hexPosition.getSouthEast()
            fullPath.add(hexPosition)
        }
    }
}
