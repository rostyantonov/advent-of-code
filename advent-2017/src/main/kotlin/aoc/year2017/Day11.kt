package aoc.year2017

import aoc.common.entity.HexPosition
import aoc.common.entity.StringConstants.NORTH
import aoc.common.entity.StringConstants.NORTH_EAST
import aoc.common.entity.StringConstants.NORTH_WEST
import aoc.common.entity.StringConstants.SOUTH
import aoc.common.entity.StringConstants.SOUTH_EAST
import aoc.common.entity.StringConstants.SOUTH_WEST
import aoc.common.entity.walker.HexWalker
import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput

class Day11 : AoCFileInput<List<String>, Int>() {
    override val inputFunction
        get() = StringInput::getFirstLineStringList

    /**
     * Crossing the bridge, you've barely reached the other side of the stream when a program comes up to you,
     * clearly in distress. "It's my child process," she says, "he's gotten lost in an infinite grid!"
     *
     * Fortunately for her, you have plenty of experience with infinite grids.
     *
     * Unfortunately for you, it's a hex grid.
     *
     * The hexagons ("hexes") in this grid are aligned such that adjacent hexes can be found to the north,
     * northeast, southeast, south, southwest, and northwest:
     *
     *        \ n  /
     *      nw +--+ ne
     *        /    \
     *      -+      +-
     *        \    /
     *      sw +--+ se
     *        / s  \
     *
     * You have the path the child process took. Starting where he started, you need to determine the fewest
     * number of steps required to reach him. (A "step" means to move from the hex you are in to any adjacent hex.)
     *
     * For example:
     *
     *     ne,ne,ne is 3 steps away.
     *     ne,ne,sw,sw is 0 steps away (back where you started).
     *     ne,ne,s,s is 2 steps away (se,se).
     *     se,sw,se,sw,sw is 3 steps away (s,s,sw).
     *
     */
    override fun processPartOne(): Int {
        val hexWalker = HexWalker()
        input.groupBy { it }.map { it.key to it.value.size }.forEach {
            when (it.first) {
                NORTH -> hexWalker.moveNorth(it.second)
                SOUTH -> hexWalker.moveSouth(it.second)
                NORTH_EAST -> hexWalker.moveNorthEast(it.second)
                SOUTH_WEST -> hexWalker.moveSouthWest(it.second)
                NORTH_WEST -> hexWalker.moveNorthWest(it.second)
                SOUTH_EAST -> hexWalker.moveSouthEast(it.second)
            }
        }
        return hexWalker.hexPosition.manhattanDistance(HexPosition(0, 0, 0))
    }
    // result 834 for part 1

    /**
     * How many steps away is the furthest he ever got from his starting position?
     */
    override fun processPartTwo(): Int {
        val hexWalker = HexWalker()
        input.forEach {
            when (it) {
                NORTH -> hexWalker.moveNorth()
                SOUTH -> hexWalker.moveSouth()
                NORTH_EAST -> hexWalker.moveNorthEast()
                SOUTH_WEST -> hexWalker.moveSouthWest()
                NORTH_WEST -> hexWalker.moveNorthWest()
                SOUTH_EAST -> hexWalker.moveSouthEast()
            }
        }
        return hexWalker.visitedPoints.maxOf { hexPosition -> hexPosition.manhattanDistance(HexPosition(0, 0, 0)) }
    }
    // result 1 569 for part 2
}
