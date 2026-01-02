package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput

class Day10 : AoCFileInput<String, Int>() {
    override var rawInput = listOf("1113222113")

    override val inputFunction
        get() = StringInput::firstString

    /**
     * Today, the Elves are playing a game called look-and-say. They take turns making sequences
     * by reading aloud the previous sequence and using that reading as the next sequence. For example,
     * 211 is read as "one two, two ones", which becomes 1221 (1 2, 2 1s).
     *
     * Look-and-say sequences are generated iteratively, using the previous value as input for the next step.
     * For each step, take the previous value, and replace each run of digits (like 111)
     * with the number of digits (3) followed by the digit itself (1).
     *
     * For example:
     *   1 becomes 11 (1 copy of digit 1).
     *   11 becomes 21 (2 copies of digit 1).
     *   21 becomes 1211 (one 2 followed by one 1).
     *   1211 becomes 111221 (one 1, one 2, and two 1s).
     *   111221 becomes 312211 (three 1s, two 2s, and one 1).
     *
     * Starting with the digits in your puzzle input, apply this process 40 times. What is the length of the result?
     */
    override fun processPartOne(): Int = taskIterator(40).length
    // result 252 594 part 1

    /**
     * Now, starting again with the digits in your puzzle input, apply this process 50 times.
     * What is the length of the new result?
     */
    override fun processPartTwo(): Int = taskIterator(50).length
    // result 3 579 328 part 2

    fun taskIterator(iterations: Int): String {
        var stringResult = StringBuilder(input)
        for (i in 1..iterations) {
            stringResult = lookAndSay(stringResult)
        }
        return stringResult.toString()
    }

    private fun lookAndSay(string: StringBuilder): StringBuilder {
        val lookAndSay = StringBuilder((string.length * 1.5).toInt())
        var firstChar = string.first()
        var firstPosition = 0
        string.forEachIndexed { i, char ->
            if (firstChar != char) {
                val len = i - firstPosition
                lookAndSay.append(len).append(firstChar)
                firstChar = char
                firstPosition = i
            }
        }
        val len = string.length - firstPosition
        lookAndSay.append(len).append(firstChar)
        return lookAndSay
    }
}
