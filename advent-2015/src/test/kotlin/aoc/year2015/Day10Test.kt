package aoc.year2015

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

// https://www.youtube.com/watch?v=ea7lJkEhytA
class Day10Test : AoCSingleFunctionTest<Day10, Int, Int, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day10()
        partOneFunc = currentDay::taskIterator
    }

    // 1 becomes 11 (1 copy of digit 1).
    // 11 becomes 21 (2 copies of digit 1).
    // 21 becomes 1211 (one 2 followed by one 1).
    // 1211 becomes 111221 (one 1, one 2, and two 1s).
    // 111221 becomes 312211 (three 1s, two 2s, and one 1).
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("11", "1", 1),
            Arguments.of("21", "11", 1),
            Arguments.of("1211", "21", 1),
            Arguments.of("111221", "1211", 1),
            Arguments.of("312211", "111221", 1),
            Arguments.of("312211", "1", 5),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(252_594, 3_579_328))
}
