package aoc.year2016

import aoc.common.entity.Position
import aoc.common.input.AoCFileInput
import aoc.common.input.IntInput

class Day13 : AoCFileInput<Int, Int>() {
    override var rawInput = listOf("1352")

    override val inputFunction
        get() = IntInput::getFirstInt

    private val wallCache = mutableMapOf<Position, Boolean>()

    /**
     * You arrive at the first floor of this new building to discover a much less welcoming environment than the
     * shiny atrium of the last one. Instead, you are in a maze of twisty little cubicles, all alike.
     *
     * Every location in this area is addressed by a pair of non-negative integers (x,y). Each such coordinate
     * is either a wall or an open space. You can't move diagonally. The cube maze starts at 0,0 and seems to
     * extend infinitely toward positive x and y; negative values are invalid, as they represent a location
     * outside the building. You are in a small waiting area at 1,1.
     *
     * While it seems chaotic, a nearby morale-boosting poster explains, the layout is actually quite logical.
     * You can determine whether a given x,y coordinate will be a wall or an open space using a simple system:
     *
     *     Find x*x + 3*x + 2*x*y + y + y*y.
     *     Add the office designer's favorite number (your puzzle input).
     *     Find the binary representation of that sum; count the number of bits that are 1.
     *         If the number of bits that are 1 is even, it's an open space.
     *         If the number of bits that are 1 is odd, it's a wall.
     *
     * For example, if the office designer's favorite number were 10, drawing walls as # and open spaces as .,
     * the corner of the building containing 0,0 would look like this:
     *
     *        0123456789
     *      0 .#.####.##
     *      1 ..#..#...#
     *      2 #....##...
     *      3 ###.#.###.
     *      4 .##..#..#.
     *      5 ..##....#.
     *      6 #...##.###
     *
     * Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
     *
     *        0123456789
     *      0 .#.####.##
     *      1 .O#..#...#
     *      2 #OOO.##...
     *      3 ###O#.###.
     *      4 .##OO#OO#.
     *      5 ..##OOO.#.
     *      6 #...##.###
     *
     * Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
     *
     * What is the fewest number of steps required for you to reach 31,39?
     *
     * Your puzzle input is 1352.
     */
    override fun processPartOne(): Int = solve(TARGET_POSITION, null)
    // result 90 for part 1

    /**
     * How many locations (distinct x,y coordinates, including your starting location) can you reach in
     * at most 50 steps?
     */
    override fun processPartTwo(): Int = solve(null, MAX_STEPS_PART_TWO)
    // result 135 for part 2

    internal fun solve(
        target: Position?,
        maxSteps: Int? = null,
    ): Int {
        val start = START_POSITION
        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(start to 0)
        val visited = mutableSetOf(start)

        while (queue.isNotEmpty()) {
            val (current, steps) = queue.removeFirst()

            // Part 1: Reach target
            if (target != null && current == target) return steps

            // Part 2: Reach max steps limit
            if (maxSteps != null && steps >= maxSteps) continue

            for (move in listOf(current.getUp(), current.getRight(), current.getDown(), current.getLeft())) {
                if (!isWall(move.row, move.col) && move !in visited) {
                    visited.add(move)
                    queue.add(move to steps + 1)
                }
            }
        }
        return if (maxSteps != null) visited.size else -1
    }

    private fun isWall(
        x: Int,
        y: Int,
    ): Boolean {
        if (x < 0 || y < 0) return true
        val pos = Position(x, y)
        return wallCache.getOrPut(pos) {
            val value = (x * x + 3 * x + 2 * x * y + y + y * y) + input
            Integer.bitCount(value) % 2 != 0
        }
    }

    companion object {
        private val START_POSITION = Position(1, 1)
        private val TARGET_POSITION = Position(31, 39)
        private const val MAX_STEPS_PART_TWO = 50
    }
}
