package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day02Test : AoCDoubleTest<Day02, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day02()
    }

    // Suppose your instructions are:
    // ULL
    // RRDDD
    // LURDL
    // UUUUD
    // in this example, the bathroom code is 1985.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("1985", testRawInput))

    // So, given the actual keypad layout, the code would be 5DB3.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of("5DB3", testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("35749", "9365C"))
}
