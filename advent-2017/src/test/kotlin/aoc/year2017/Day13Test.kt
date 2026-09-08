package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day13Test :
    AoCEmptyTest<Day13, Int>(),
    IAoCDoubleTest<Day13, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day13()
    }

    // For example, suppose you've recorded the following:
    //      0: 3
    //      1: 2
    //      4: 4
    //      6: 4
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(24, testRawInput))

    // In the example above, if you delay 10 picoseconds (picoseconds 0 - 9), you won't get caught
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(10, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_624, 3_923_436))
}
