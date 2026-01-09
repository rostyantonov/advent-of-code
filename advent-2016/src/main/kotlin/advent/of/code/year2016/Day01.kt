package advent.of.code.year2016

import advent.of.code.common.Solution
import advent.of.code.common.AdventSolution
import kotlin.math.abs

/**
 * Solution for Advent of Code 2016, Day 1: No Time for a Taxicab
 * 
 * Problem: You're following instructions to navigate through a city grid.
 * Starting at (0, 0) facing North, instructions like "R2" mean turn right and walk 2 blocks.
 * 
 * Part 1: How many blocks away is Easter Bunny HQ?
 * Part 2: How many blocks away is the first location you visit twice?
 */
@AdventSolution(year = 2016, day = 1)
class Day01 : Solution {
    private enum class Direction(val dx: Int, val dy: Int) {
        NORTH(0, 1), EAST(1, 0), SOUTH(0, -1), WEST(-1, 0);

        fun turnRight() = values()[(ordinal + 1) % 4]
        fun turnLeft() = values()[(ordinal + 3) % 4]
    }

    override fun solvePart1(input: String): String {
        var x = 0
        var y = 0
        var direction = Direction.NORTH

        input.split(", ").forEach { instruction ->
            direction = if (instruction[0] == 'R') direction.turnRight() else direction.turnLeft()
            val steps = instruction.substring(1).toInt()
            x += direction.dx * steps
            y += direction.dy * steps
        }

        return (abs(x) + abs(y)).toString()
    }

    override fun solvePart2(input: String): String {
        var x = 0
        var y = 0
        var direction = Direction.NORTH
        val visited = mutableSetOf(Pair(0, 0))

        input.split(", ").forEach { instruction ->
            direction = if (instruction[0] == 'R') direction.turnRight() else direction.turnLeft()
            val steps = instruction.substring(1).toInt()
            
            repeat(steps) {
                x += direction.dx
                y += direction.dy
                if (!visited.add(Pair(x, y))) {
                    return (abs(x) + abs(y)).toString()
                }
            }
        }

        return "Not found"
    }
}
