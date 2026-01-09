package advent.of.code.common

/**
 * Base interface for all Advent of Code solutions.
 * Each solution should implement this interface.
 */
interface Solution {
    /**
     * Solves part 1 of the puzzle.
     * @param input The puzzle input as a string
     * @return The solution as a string
     */
    fun solvePart1(input: String): String

    /**
     * Solves part 2 of the puzzle.
     * @param input The puzzle input as a string
     * @return The solution as a string
     */
    fun solvePart2(input: String): String
}
