package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day14Test :
    AoCEmptyTest<Day14, Int>(),
    IAoCDoubleTest<Day14, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day14()
    }

    // For example, if your key string were flqrgnkx, then the first row would be given by the bits of the
    // knot hash of flqrgnkx-0, the second row from the bits of the knot hash of flqrgnkx-1, and so on until
    // the last row, flqrgnkx-127.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(8_108, "flqrgnkx"))

    // In total, in this example, 1242 regions are present.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(1_242, "flqrgnkx"))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(8_304, 1_018))
}
