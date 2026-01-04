package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.CharInput
import aoc.common.entity.Direction.LEFT_BRACE as UP_DIRECTION
import aoc.common.entity.Direction.RIGHT_BRACE as DOWN_DIRECTION

class Day01 : AoCFileInput<List<Char>, Int>() {
    override val inputFunction
        get() = CharInput::stringAsCharList

    /**
     * Santa is trying to deliver presents in a large apartment building,
     * but he can't find the right floor - the directions he got are a little confusing.
     * He starts on the ground floor (floor 0) and then follows the instructions one character at a time.
     *
     * An opening parenthesis, (, means he should go up one floor,
     * and a closing parenthesis, ), means he should go down one floor.
     *
     * The apartment building is very tall, and the basement is very deep; he will never find the top or bottom floors.
     *
     * For example:
     *
     *      (()) and ()() both result in floor 0.
     *      ((( and (()(()( both result in floor 3.
     *      ))((((( also results in floor 3.
     *      ()) and ))( both result in floor -1 (the first basement level).
     *      ))) and )())()) both result in floor -3.
     *
     * To what floor do the instructions take Santa?
     */
    override fun processPartOne(): Int =
        input.sumOf { direction ->
            when (direction) {
                UP_DIRECTION -> 1
                DOWN_DIRECTION -> -1
                else -> 0
            }
        }
    // result 280 for part 1

    /**
     * Now, given the same instructions,
     * find the position of the first character that causes him to enter the basement (floor -1).
     * The first character in the instructions has position 1, the second character has position 2, and so on.
     *
     * For example:
     *
     *      ) causes him to enter the basement at character position 1.
     *      ()()) causes him to enter the basement at character position 5.
     *
     * What is the position of the character that causes Santa to first enter the basement?
     */
    override fun processPartTwo(): Int {
        var floor = 0
        input.forEachIndexed { index, direction ->
            when (direction) {
                UP_DIRECTION -> floor++
                DOWN_DIRECTION -> floor--
            }
            if (floor < 0) {
                return index + 1
            }
        }
        return -1
    }
    // result 1 797 for part 2
}
