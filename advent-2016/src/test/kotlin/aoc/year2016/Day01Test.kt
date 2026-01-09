package aoc.year2016

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
    //    Following R2, L3 leaves you 2 blocks East and 3 blocks North, or 5 blocks away.
    //    R2, R2, R2 leaves you 2 blocks due South of your starting position, which is 2 blocks away.
    //    R5, L5, R5, R3 leaves you 12 blocks away.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(5, "R2, L3"),
            Arguments.of(2, "R2, R2, R2"),
            Arguments.of(12, "R5, L5, R5, R3"),
        )

    // For example, if your instructions are R8, R4, R4, R8,
    // the first location you visit twice is 4 blocks away, due East.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(4, "R8, R4, R4, R8"))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(291, 159))
}
