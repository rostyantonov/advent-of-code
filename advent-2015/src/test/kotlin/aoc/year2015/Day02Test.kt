package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day02Test : AoCDoubleTest<Day02, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day02()
    }

    // A present with dimensions 2x3x4 requires 2*6 + 2*12 + 2*8 = 52 square feet of wrapping paper
    //   plus 6 square feet of slack, for a total of 58 square feet.
    // A present with dimensions 1x1x10 requires 2*1 + 2*10 + 2*10 = 42 square feet of wrapping paper
    //   plus 1 square foot of slack, for a total of 43 square feet.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(58, "2x3x4"),
            Arguments.of(43, "1x1x10"),
        )

    // A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present
    //   plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
    // A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present
    //   plus 1*1*10 = 10 feet of ribbon for the bow, for a total of 14 feet.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(34, "2x3x4"),
            Arguments.of(14, "1x1x10"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_606_483, 3_842_356))
}
