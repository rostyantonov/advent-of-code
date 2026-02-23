package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class Day23Test : AoCDoubleTest<Day23, Int>() {
    @BeforeEach
    override fun setupCurrentDay() {
        currentDay = Day23()
    }

    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, testRawInput),
        )

    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, testRawInput), // Test input always results in 3 (starts with cpy 2 a)
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(12_560, 479_009_120), // Correct result with optimizations
        )
}
