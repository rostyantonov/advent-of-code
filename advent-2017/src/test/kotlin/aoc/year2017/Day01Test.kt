package aoc.year2017

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day01Test : AoCDoubleTest<Day01, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day01()
    }

    // For example:
    //    1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the second digit and the third
    //      digit (2) matches the fourth digit.
    //    1111 produces 4 because each digit (all 1) matches the next.
    //    1234 produces 0 because no digit matches the next.
    //    91212129 produces 9 because the only digit that matches the next one is the last digit, 9.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, "1122"),
            Arguments.of(4, "1111"),
            Arguments.of(0, "1234"),
            Arguments.of(9, "91212129"),
        )

    // For example:
    //    1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
    //    1221 produces 0, because every comparison is between a 1 and a 2.
    //    123425 produces 4, because both 2s match each other, but no other digit has a match.
    //    123123 produces 12.
    //    12131415 produces 4.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6, "1212"),
            Arguments.of(0, "1221"),
            Arguments.of(4, "123425"),
            Arguments.of(12, "123123"),
            Arguments.of(4, "12131415"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_171, 1_024))
}
