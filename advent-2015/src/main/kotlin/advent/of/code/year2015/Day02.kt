package advent.of.code.year2015

import advent.of.code.common.Solution
import advent.of.code.common.AdventSolution

/**
 * Solution for Advent of Code 2015, Day 2: I Was Told There Would Be No Math
 * 
 * Problem: The elves need to order wrapping paper for presents.
 * Each present is a box with dimensions l×w×h. The wrapping paper needed for each
 * present is the surface area of the box plus the area of the smallest side.
 * 
 * Part 1: How many total square feet of wrapping paper should they order?
 * Part 2: How many total feet of ribbon should they order?
 */
@AdventSolution(year = 2015, day = 2)
class Day02 : Solution {
    override fun solvePart1(input: String): String {
        val total = input.lines()
            .filter { it.isNotEmpty() }
            .sumOf { line ->
                val (l, w, h) = line.split("x").map { it.toInt() }
                val sides = listOf(l * w, w * h, h * l)
                2 * sides.sum() + sides.min()
            }
        return total.toString()
    }

    override fun solvePart2(input: String): String {
        val total = input.lines()
            .filter { it.isNotEmpty() }
            .sumOf { line ->
                val dimensions = line.split("x").map { it.toInt() }.sorted()
                val ribbon = 2 * (dimensions[0] + dimensions[1])
                val bow = dimensions[0] * dimensions[1] * dimensions[2]
                ribbon + bow
            }
        return total.toString()
    }
}
