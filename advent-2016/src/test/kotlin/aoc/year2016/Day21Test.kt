package aoc.year2016

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day21Test :
    AoCEmptyTest<Day21, String>(),
    IAoCSingleFunctionTest<Day21, String, String, String> {
    override val partOneFunc: (String) -> String
        get() = currentDay::scramble

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day21()
    }

    // Example from problem statement:
    // Starting with "abcde" and applying operations results in "decab"
    // Test input is loaded from day21test.txt
    override fun partOneFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("decab", testRawInput, "abcde"),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of("gfdhebac", "dhaegfbc"),
        )
}
