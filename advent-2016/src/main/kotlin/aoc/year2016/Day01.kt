package aoc.year2016

import aoc.common.entity.Direction.LEFT_CHAR
import aoc.common.entity.Direction.RIGHT_CHAR
import aoc.common.entity.Position
import aoc.common.entity.walker.FacingWalker
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2016.entity.WalkerInstruction
import kotlin.math.abs

class Day01 : AoCFileInput<List<WalkerInstruction>, Int>() {
    override val inputFunction
        get() = StructuredInput(
            regex = Regex("(?<direction>[LR])(?<steps>\\d+)"),
            builder = WalkerInstruction::fromLine,
        )::getSingleStructInput

    /**
     * You're airdropped near Easter Bunny Headquarters in a city somewhere. "Near", unfortunately, is as close as
     * you can get - the instructions on the Easter Bunny Recruiting Document the Elves intercepted start here,
     * and nobody had time to work them out further.
     *
     * The Document indicates that you should start at the given coordinates (where you just landed) and face North.
     * Then, follow the provided sequence: either turn left (L) or right (R) 90 degrees,
     * then walk forward the given number of blocks, ending at a new intersection.
     *
     * There's no time to follow such ridiculous instructions on foot, though,
     * so you take a moment and work out the destination. Given that you can only walk on the street grid of the city,
     * how far is the shortest path to the destination?
     *
     * For example:
     *
     *     Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
     *     R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
     *     R5, L5, R5, R3 leaves you 12 blocks away.
     *
     * How many blocks away is Easter Bunny HQ?
     */
    override fun processPartOne(): Int {
        val walker = FacingWalker()
        input.forEach { instruction ->
            when (instruction.direction) {
                LEFT_CHAR -> walker.turnLeft()
                RIGHT_CHAR -> walker.turnRight()
            }
            walker.moveForward(instruction.steps)
        }
        return walker.position.manhattanDistance(Position(0, 0))
        // result 291 for part 1
    }

    /**
     * Then, you notice the instructions continue on the back of the Recruiting Document.
     * Easter Bunny HQ is actually at the first location you visit twice.
     *
     * For example, if your instructions are R8, R4, R4, R8, the first location you visit twice is 4 blocks away,
     * due East.
     *
     * How many blocks away is the first location you visit twice?
     */
    override fun processPartTwo(): Int {
        val walker = FacingWalker()
        input.forEach { instruction ->
            when (instruction.direction) {
                LEFT_CHAR -> walker.turnLeft()
                RIGHT_CHAR -> walker.turnRight()
            }
            walker.moveForward(instruction.steps)
        }
        val visited = mutableSetOf<Position>()
        walker.fullPath.forEach { position ->
            // point already in set
            if (!visited.add(position)) {
                // should be exit point
                return position.manhattanDistance(Position(0, 0))
            }
        }
        return walker.position.manhattanDistance(Position(0, 0))
        // result 159 for part 2
    }
}
