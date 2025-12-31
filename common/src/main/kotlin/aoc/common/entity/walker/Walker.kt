package aoc.common.entity.walker

import aoc.common.entity.Position

open class Walker(
    open var position: Position = Position(0, 0),
) {
    private val fullPath: MutableList<Position> = mutableListOf(position)

    val visitedPoints: Set<Position>
        get() = fullPath.toSet()

    fun moveUp(steps: Int = 1) {
        repeat(steps) {
            position = position.getUp()
            fullPath.add(position)
        }
    }

    fun moveDown(steps: Int = 1) {
        repeat(steps) {
            position = position.getDown()
            fullPath.add(position)
        }
    }

    fun moveLeft(steps: Int = 1) {
        repeat(steps) {
            position = position.getLeft()
            fullPath.add(position)
        }
    }

    fun moveRight(steps: Int = 1) {
        repeat(steps) {
            position = position.getRight()
            fullPath.add(position)
        }
    }
}
