package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day03Test : AoCDoubleTest<Day03, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day03()
    }

    // For example, the "triangle" given above is impossible, because 5 + 10 is not larger than 25.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(0, "5 10 25"))

    // A present with dimensions 2x3x4 requires 2+2+3+3 = 10 feet of ribbon to wrap the present
    //   plus 2*3*4 = 24 feet of ribbon for the bow, for a total of 34 feet.
    // A present with dimensions 1x1x10 requires 1+1+1+1 = 4 feet of ribbon to wrap the present
    //   plus 1*1*10 = 10 feet of ribbon for the bow, for a total of 14 feet.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(6, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(917, 1_649))
}
