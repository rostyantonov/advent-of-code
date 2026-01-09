package aoc.common.entity.walker

import aoc.common.entity.Position
import aoc.common.grid.GridDirection
import aoc.common.grid.GridDirection.DOWN
import aoc.common.grid.GridDirection.LEFT
import aoc.common.grid.GridDirection.RIGHT
import aoc.common.grid.GridDirection.UP

class FacingWalker(
    position: Position = Position(0, 0),
    var facingDirection: GridDirection = UP,
) : Walker(position) {
    fun moveForward(steps: Int = 1) {
        when (facingDirection) {
            UP -> repeat(steps) { moveUp() }
            LEFT -> repeat(steps) { moveLeft() }
            RIGHT -> repeat(steps) { moveRight() }
            DOWN -> repeat(steps) { moveDown() }
        }
    }

    fun turnRight() {
        facingDirection =
            when (facingDirection) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
    }

    fun turnLeft() {
        facingDirection =
            when (facingDirection) {
                UP -> LEFT
                LEFT -> DOWN
                DOWN -> RIGHT
                RIGHT -> UP
            }
    }

    fun facingPosition(): Position =
        when (facingDirection) {
            UP -> position.getUp()
            LEFT -> position.getLeft()
            RIGHT -> position.getRight()
            DOWN -> position.getDown()
        }
}
