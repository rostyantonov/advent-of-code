package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day15Test :
    AoCEmptyTest<Day15, Int>(),
    IAoCDoubleTest<Day15, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day15()
    }

    // For example, suppose that for starting values, generator A uses 65, while generator B uses 8921.
    // In the example above, the judge would eventually find a total of 588 pairs that match in their lowest 16 bits.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(588, testRawInput))

    // Using the values from the example above, after five million pairs, the judge would eventually find a
    // total of 309 pairs that match in their lowest 16 bits.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(309, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(631, 279))
}
