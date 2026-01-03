package aoc.year2015

import aoc.common.grid.GridArray
import aoc.common.input.AoCFileInput
import aoc.common.input.CharInput
import aoc.common.util.set

class Day18 : AoCFileInput<GridArray<Char>, Int>() {
    override val inputFunction
        get() = CharInput::getGridArray

    private lateinit var myInput: GridArray<Char>

    /**
     * After the million lights incident, the fire code has gotten stricter:
     * now, at most ten thousand lights are allowed. You arrange them in a 100x100 grid.
     *
     * Never one to let you down, Santa again mails you instructions on the ideal lighting configuration.
     * With so few lights, he says, you'll have to resort to animation.
     *
     * Start by setting your lights to the included initial configuration (your puzzle input).
     * A # means "on", and a . means "off".
     *
     * Then, animate your grid in steps, where each step decides the next configuration based on the current one.
     * Each light's next state (either on or off) depends on its current state and the current states
     * of the eight lights adjacent to it (including diagonals). Lights on the edge of the grid might have fewer
     * than eight neighbors; the missing ones always count as "off".
     *
     * For example, in a simplified 6x6 grid, the light marked A has the neighbors numbered 1 through 8,
     * and the light marked B, which is on an edge, only has the neighbors marked 1 through 5:
     *
     *      1B5...
     *      234...
     *      ......
     *      ..123.
     *      ..8A4.
     *      ..765.
     *
     * The state a light should have next is based on its current state (on or off)
     * plus the number of neighbors that are on:
     *
     *     A light which is on stays on when 2 or 3 neighbors are on, and turns off otherwise.
     *     A light which is off turns on if exactly 3 neighbors are on, and stays off otherwise.
     *
     * All of the lights update simultaneously; they all consider the same current state before moving to the next.
     *
     * Here's a few steps from an example configuration of another 6x6 grid:
     *
     * Initial state:
     *
     *      .#.#.#
     *      ...##.
     *      #....#
     *      ..#...
     *      #.#..#
     *      ####..
     *
     * After 4 steps:
     *
     *      ......
     *      ......
     *      ..##..
     *      ..##..
     *      ......
     *      ......
     *
     * After 4 steps, this example has four lights on.
     *
     * In your grid of 100x100 lights, given your initial configuration, how many lights are on after 100 steps?
     */
    override fun processPartOne(): Int = checkPartOne(SIMULATION_STEPS)
    // result 1 061 for part 1

    /**
     * You flip the instructions over; Santa goes on to point out that this is all just an implementation of
     * Conway's Game of Life. At least, it was, until you notice that something's wrong with the grid of lights
     * you bought: four lights, one in each corner, are stuck on and can't be turned off.
     * The example above will actually run like this:
     *
     * After 5 steps:
     *
     *      ##.###
     *      .##..#
     *      .##...
     *      .##...
     *      #.#...
     *      ##...#
     *
     * After 5 steps, this example now has 17 lights on.
     *
     * In your grid of 100x100 lights, given your initial configuration, but with the four corners always
     * in the on state, how many lights are on after 100 steps?
     */
    override fun processPartTwo(): Int = checkPartTwo(SIMULATION_STEPS)
    // result 1 006 for part 2

    fun checkPartOne(steps: Int): Int {
        myInput = input.clone()
        return iterateSteps(steps)
    }

    fun checkPartTwo(steps: Int): Int {
        myInput = input.clone()
        with(myInput.gridData) {
            val col = first().size - 1
            first()[0] = LIGHT_STAR
            first()[col] = LIGHT_STAR
            last()[0] = LIGHT_STAR
            last()[col] = LIGHT_STAR
        }
        return iterateSteps(steps)
    }

    private fun iterateSteps(steps: Int): Int {
        var newInput = myInput.clone()
        repeat(steps) {
            myInput.gridData.forEachIndexed { row, rowData ->
                rowData.forEachIndexed { col, char ->
                    newInput.gridData[row, col] = nextState(char, row, col)
                }
            }
            val tmp = myInput
            myInput = newInput
            newInput = tmp
        }
        return myInput.gridData.sumOf { row -> row.count { it == LIGHT_ON || it == LIGHT_STAR } }
    }

    private fun nextState(current: Char, row: Int, col: Int): Char {
        if (current == LIGHT_STAR) return LIGHT_STAR
        
        val neighborCount = myInput.getNeighbours(row, col ).count { it == LIGHT_ON || it == LIGHT_STAR }
        return when (current) {
            LIGHT_ON -> if (neighborCount == 2 || neighborCount == 3) LIGHT_ON else LIGHT_OFF
            else -> if (neighborCount == 3) LIGHT_ON else LIGHT_OFF
        }
    }

    // A # means "on", and a . means "off".
    companion object {
        const val LIGHT_ON = '#'
        const val LIGHT_OFF = '.'

        // for always on
        const val LIGHT_STAR = '*'
        
        private const val SIMULATION_STEPS = 100
    }
}