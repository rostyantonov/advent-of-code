package aoc.year2017

import aoc.common.entity.HexPosition
import aoc.common.entity.walker.HexWalker
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2017.entity.HexDirection
import aoc.year2017.entity.HexDirection.N
import aoc.year2017.entity.HexDirection.NE
import aoc.year2017.entity.HexDirection.NW
import aoc.year2017.entity.HexDirection.S
import aoc.year2017.entity.HexDirection.SE
import aoc.year2017.entity.HexDirection.SW
import aoc.year2017.entity.HexDirectionCompanion

class Day11 : AoCFileInput<List<HexDirection>, Int>() {
    override val inputFunction
        get() =
            StructuredInput
                .of(
                    structure = HexDirectionCompanion,
                    splitBy = ",",
                )::getFirstLineStructInput

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
                N -> hexWalker.moveNorth(it.second)
                S -> hexWalker.moveSouth(it.second)
                NE -> hexWalker.moveNorthEast(it.second)
                SW -> hexWalker.moveSouthWest(it.second)
                NW -> hexWalker.moveNorthWest(it.second)
                SE -> hexWalker.moveSouthEast(it.second)
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
                N -> hexWalker.moveNorth()
                S -> hexWalker.moveSouth()
                NE -> hexWalker.moveNorthEast()
                SW -> hexWalker.moveSouthWest()
                NW -> hexWalker.moveNorthWest()
                SE -> hexWalker.moveSouthEast()
            }
        }
        return hexWalker.visitedPoints.maxOf { hexPosition -> hexPosition.manhattanDistance(HexPosition(0, 0, 0)) }
    }
    // result 1 569 for part 2
}
