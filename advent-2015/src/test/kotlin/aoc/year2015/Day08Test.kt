package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day08Test : AoCDoubleTest<Day08, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day08()
    }

    // "" is 2 characters of code (the two double quotes), but the string contains zero characters.
    // "abc" is 5 characters of code, but 3 characters in the string data.
    // "aaa\"aaa" is 10 characters of code, but the string itself contains six "a" characters and a single,
    //   escaped quote character, for a total of 7 characters in the string data.
    // "\x27" is 6 characters of code, but the string itself contains just one - an apostrophe ('),
    //   escaped using hexadecimal notation.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(12, testRawInput))

    // "" encodes to "\"\"", an increase from 2 characters to 6.
    // "abc" encodes to "\"abc\"", an increase from 5 characters to 9.
    // "aaa\"aaa" encodes to "\"aaa\\\"aaa\"", an increase from 10 characters to 16.
    // "\x27" encodes to "\"\\x27\"", an increase from 6 characters to 11.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(19, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_350, 2_085))
}
