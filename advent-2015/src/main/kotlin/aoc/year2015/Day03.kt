package aoc.year2015

import aoc.common.entity.Direction.DOWN_ARROW
import aoc.common.entity.Direction.LEFT_ARROW
import aoc.common.entity.Direction.RIGHT_ARROW
import aoc.common.entity.Direction.UP_ARROW
import aoc.common.entity.walker.Walker
import aoc.common.input.AoCFileInput
import aoc.common.input.CharInput

class Day03 : AoCFileInput<List<Char>, Int>() {
    override val inputFunction
        get() = CharInput::stringAsCharList

    /**
     * Santa is delivering presents to an infinite two-dimensional grid of houses.
     *
     * He begins by delivering a present to the house at his starting location,
     * and then an elf at the North Pole calls him via radio and tells him where to move next.
     * Moves are always exactly one house to the north (^), south (v), east (>), or west (<).
     * After each move, he delivers another present to the house at his new location.
     *
     * However, the elf back at the north pole has had a little too much eggnog,
     * and so his directions are a little off, and Santa ends up visiting some houses more than once.
     * How many houses receive at least one present?
     *
     * For example:
     *   > delivers presents to 2 houses: one at the starting location, and one to the east.
     *   ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
     *   ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
     */
    override fun processPartOne(): Int {
        val santa = Walker()

        input.forEach { direction ->
            when (direction) {
                UP_ARROW -> santa.moveUp()
                DOWN_ARROW -> santa.moveDown()
                LEFT_ARROW -> santa.moveLeft()
                RIGHT_ARROW -> santa.moveRight()
            }
        }
        return santa.visitedPoints.size
    }
    // result 2 572 for part 1

    /**
     * The next year, to speed up the process, Santa creates a robot version of himself,
     * Robo-Santa, to deliver presents with him.
     *
     * Santa and Robo-Santa start at the same location (delivering two presents to the same starting house),
     * then take turns moving based on instructions from the elf,
     * who is eggnoggedly reading from the same script as the previous year.
     *
     * This year, how many houses receive at least one present?
     *
     * For example:
     *   ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
     *   ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
     *   ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
     */
    override fun processPartTwo(): Int {
        val santa = Walker()
        val roboSanta = Walker()

        input.forEachIndexed { index, direction ->
            if (index % 2 == 1) {
                roboSanta
            } else {
                santa
            }.let { currentSanta ->
                when (direction) {
                    UP_ARROW -> currentSanta.moveUp()
                    DOWN_ARROW -> currentSanta.moveDown()
                    LEFT_ARROW -> currentSanta.moveLeft()
                    RIGHT_ARROW -> currentSanta.moveRight()
                }
            }
        }
        return buildSet {
            addAll(santa.visitedPoints)
            addAll(roboSanta.visitedPoints)
        }.size
    }
    // result 2 631 for part 2
}
