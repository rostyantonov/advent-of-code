package aoc.year2016

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day21Test : AoCSingleFunctionTest<Day21, String, String, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day21()
        partOneFunc = currentDay::scramble
    }

    // Example from problem statement:
    // Starting with "abcde" and applying operations results in "decab"
    // Test input is loaded from day21test.txt
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("decab", testRawInput, "abcde"),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of("gfdhebac", "dhaegfbc"),
        )
}
