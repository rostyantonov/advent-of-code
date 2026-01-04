package aoc.common.entity.walker

import aoc.common.entity.Position

open class Walker(
    var position: Position = Position(0, 0),
) {
    private val _fullPath: MutableList<Position> = mutableListOf(position)

    val fullPath: List<Position>
        get() = _fullPath

    val visitedPoints: Set<Position>
        get() = _fullPath.toSet()

    fun moveUp(steps: Int = 1) {
        repeat(steps) {
            position = position.getUp()
            _fullPath.add(position)
        }
    }

    fun moveDown(steps: Int = 1) {
        repeat(steps) {
            position = position.getDown()
            _fullPath.add(position)
        }
    }

    fun moveLeft(steps: Int = 1) {
        repeat(steps) {
            position = position.getLeft()
            _fullPath.add(position)
        }
    }

    fun moveRight(steps: Int = 1) {
        repeat(steps) {
            position = position.getRight()
            _fullPath.add(position)
        }
    }
}
