package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class Day22Test : AoCDoubleTest<Day22, Int>() {
    @BeforeEach
    override fun setupCurrentDay() {
        currentDay = Day22()
    }

    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(7, testRawInput),
        )

    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(7, testRawInput),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(903, 215),
        )
}
