package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.common.util.reverse
import aoc.year2015.entity.Connection
import aoc.year2015.entity.ConnectionCompanion
import aoc.year2015.entity.Path

// https://en.wikipedia.org/wiki/Travelling_salesman_problem
class Day09 : AoCFileInput<List<Connection>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex = Regex("(?<from>\\w+) to (?<to>\\w+) = (?<value>\\d+)"),
                builder = ConnectionCompanion::fromLine,
            )::getStructInput

    private val links: List<Path> by lazy {
        Path.getAllPaths(
            // add both first and last points, and make it distinct
            (input.map { it.fromTo.first } + input.map { it.fromTo.second }).distinct(),
            // create map mor both A->B and B->A directions
            buildMap {
                input.forEach {
                    put(it.fromTo, it.value)
                    put(it.fromTo.reverse(), it.value)
                }
            },
        )
    }

    /**
     * Every year, Santa manages to deliver all of his presents in a single night.
     *
     * This year, however, he has some new locations to visit;
     * his elves have provided him the distances between every pair of locations.
     * He can start and end at any two (different) locations he wants, but he must visit each location exactly once.
     * What is the shortest distance he can travel to achieve this?
     *
     * For example, given the following distances:
     *   London to Dublin = 464
     *   London to Belfast = 518
     *   Dublin to Belfast = 141
     *
     * The possible routes are therefore:
     *   Dublin -> London -> Belfast = 982
     *   London -> Dublin -> Belfast = 605
     *   London -> Belfast -> Dublin = 659
     *   Dublin -> Belfast -> London = 659
     *   Belfast -> Dublin -> London = 605
     *   Belfast -> London -> Dublin = 982
     *
     * The shortest of these is London -> Dublin -> Belfast = 605, and so the answer is 605 in this example.
     *
     * What is the distance of the shortest route?
     */
    override fun processPartOne(): Int = links.minBy { it.distance }.distance
    // result 141 for part 1

    /**
     * The next year, just to show off, Santa decides to take the route with the longest distance instead.
     *
     * He can still start and end at any two (different) locations he wants,
     * and he still must visit each location exactly once.
     * For example, given the distances above, the longest route would be 982 via (for example) Dublin -> London -> Belfast.
     *
     * What is the distance of the longest route?
     */
    override fun processPartTwo(): Int = links.maxBy { it.distance }.distance
    // result 736 for part 2
}
