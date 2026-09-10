package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day17Test :
    AoCEmptyTest<Day17, Int>(),
    IAoCSingleTest<Day17, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day17()
    }

    // For example, if the spinlock were to step 3 times per insert, the circular buffer would begin to evolve
    // like this (using parentheses to mark the current position after each iteration of the algorithm):
    //    (0)
    //    0 (1)
    //    0 (2) 1
    //    0  2 (3) 1
    //    0  2 (4) 3  1
    //    0 (5) 2  4  3  1
    //    0  5  2  4  3 (6) 1
    //    0  5 (7) 2  4  3  6  1
    //    0  5  7  2  4  3 (8) 6  1
    //    0 (9) 5  7  2  4  3  8  6  1
    //
    // Eventually, after 2017 insertions, the section of the circular buffer near the last insertion looks like this:
    // 1512  1134  151 (2017) 638  1513  851
    //
    // Perhaps, if you can identify the value that will ultimately be after the last value written (2017), you
    // can short-circuit the spinlock. In this example, that would be 638.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(638, 3))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_311, 39_170_601))
}
