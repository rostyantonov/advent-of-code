package aoc.year2016

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class Day24Test :
    AoCEmptyTest<Day24, Int>(),
    IAoCDoubleTest<Day24, Int> {
    @BeforeEach
    override fun setupCurrentDay() {
        currentDay = Day24()
    }

    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(14, testRawInput),
        )

    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(20, testRawInput),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(464, 652),
        )
}
