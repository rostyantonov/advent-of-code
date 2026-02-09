package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput
import aoc.common.util.rotateBottom
import aoc.common.util.rotateRight
import aoc.common.util.set
import aoc.common.util.toDisplayChars
import aoc.year2016.entity.Card
import aoc.year2016.entity.Card.Column
import aoc.year2016.entity.Card.Rect
import aoc.year2016.entity.Card.Row
import aoc.year2016.entity.CardCompanion

class Day08 : AoCFileInput<List<Card>, String>() {
    override val inputFunction
        get() =
            StructuredMultiInput(
                regexArray =
                    arrayOf(
                        Regex("(?<cmd>rect) (?<xPos>\\d+)x(?<yPos>\\d+)"),
                        Regex("rotate (?<cmd>column) x=(?<column>\\d+) by (?<amount>\\d+)"),
                        Regex("rotate (?<cmd>row) y=(?<row>\\d+) by (?<amount>\\d+)"),
                    ),
                builder = CardCompanion::fromLine,
            )::getStructInput

    private val width: Int = 50
    private val height: Int = 6
    private lateinit var screen: Array<BooleanArray>

    /**
     * You come across a door implementing what you can only assume is an implementation
     * of two-factor authentication after a long game of requirements telephone.
     *
     * To get past the door, you first swipe a keycard (no problem; there was one on a nearby desk).
     * Then, it displays a code on a little screen, and you type that code on a keypad.
     * Then, presumably, the door unlocks.
     *
     * Unfortunately, the screen has been smashed. After a few minutes, you've taken everything apart
     * and figured out how it works. Now you just have to work out what the screen would have displayed.
     *
     * The magnetic strip on the card you swiped encodes a series of instructions for the screen;
     * these instructions are your puzzle input. The screen is 50 pixels wide and 6 pixels tall,
     * all of which start off, and is capable of three somewhat peculiar operations:
     * * rect AxB turns on all of the pixels in a rectangle at the top-left of the screen which is A wide and B tall.
     * * rotate row y=A by B shifts all of the pixels in row A (0 is the top row) right by B pixels.
     *      Pixels that would fall off the right end appear at the left end of the row.
     * * rotate column x=A by B shifts all of the pixels in column A (0 is the left column) down by B pixels.
     *      Pixels that would fall off the bottom appear at the top of the column.
     *
     * For example, here is a simple sequence on a smaller screen:
     *   rect 3x2 creates a small rectangle in the top-left corner:
     *
     *       ###....
     *       ###....
     *       .......
     *
     *  rotate column x=1 by 1 rotates the second column down by one pixel:
     *
     *       #.#....
     *       ###....
     *       .#.....
     *
     *  rotate row y=0 by 4 rotates the top row right by four pixels:
     *
     *     ....#.#
     *     ###....
     *     .#.....
     *
     *  rotate column x=1 by 1 again rotates the second column down by one pixel, causing the bottom pixel to
     *  wrap back to the top:
     *
     *     .#..#.#
     *     #.#....
     *     .#.....
     *
     * As you can see, this display technology is extremely powerful,
     * and will soon dominate the tiny-code-displaying-screen market.
     * That's what the advertisement on the back of the display tries to convince you, anyway.
     *
     * There seems to be an intermediate check of the voltage used by the display:
     * after you swipe your card, if the screen did work, how many pixels should be lit?
     */
    override fun processPartOne(): String = doPartOne(Pair(width, height))
    // result 119 for part 1

    internal fun doPartOne(dimensions: Pair<Int, Int>): String {
        doInstructions(dimensions)
        return screen
            .sumOf { yLine ->
                yLine.count { it }
            }.toString()
    }

    private fun doInstructions(dimensions: Pair<Int, Int>) {
        screen =
            Array(dimensions.second) {
                BooleanArray(dimensions.first) { false }
            }
        input.forEach { instr ->
            when (instr) {
                is Rect -> screen[0..<instr.yPos, 0..<instr.xPos] = true
                is Column -> screen.rotateBottom(instr.column, instr.amount)
                is Row -> screen.rotateRight(instr.row, instr.amount)
            }
        }
    }

    /**
     * You notice that the screen is only capable of displaying capital letters;
     * in the font it uses, each letter is 5 pixels wide and 6 tall.
     *
     * After you swipe your card, what code is the screen trying to display?
     */
    override fun processPartTwo(): String {
        doInstructions(Pair(width, height))
        return screen.toDisplayChars(5, 6)
    }
    // result ZFHFSFOGPO for part 2
}
