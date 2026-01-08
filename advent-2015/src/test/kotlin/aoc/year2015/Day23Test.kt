package aoc.year2015

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day23Test :
    AoCEmptyTest<Day23, Int>(),
    IAoCSingleTest<Day23, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day23()
    }

    // For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
    //      inc b
    //      jio b, +2
    //      tpl b
    //      inc b
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(2, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(255, 334))
}
