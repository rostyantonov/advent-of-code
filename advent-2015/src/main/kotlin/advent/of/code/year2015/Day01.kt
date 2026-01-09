package advent.of.code.year2015

import advent.of.code.common.Solution
import advent.of.code.common.AdventSolution

/**
 * Solution for Advent of Code 2015, Day 1: Not Quite Lisp
 * 
 * Problem: Santa is trying to deliver presents in a large apartment building,
 * but he can't find the right floor. Starting on floor 0, an opening parenthesis '('
 * means he should go up one floor, and a closing parenthesis ')' means he should go down one floor.
 * 
 * Part 1: To what floor do the instructions take Santa?
 * Part 2: What is the position of the character that causes Santa to first enter the basement (floor -1)?
 */
@AdventSolution(year = 2015, day = 1)
class Day01 : Solution {
    override fun solvePart1(input: String): String {
        val floor = input.count { it == '(' } - input.count { it == ')' }
        return floor.toString()
    }

    override fun solvePart2(input: String): String {
        var floor = 0
        for ((index, char) in input.withIndex()) {
            floor += if (char == '(') 1 else -1
            if (floor == -1) {
                return (index + 1).toString()
            }
        }
        return "Not found"
    }
}
