package aoc.year2017

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day08Test : AoCDoubleTest<Day08, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day08()
    }

    // The instructions look like this:
    //      b inc 5 if a > 1
    //      a inc 1 if b < 5
    //      c dec -10 if a >= 1
    //      c inc -20 if c == 10
    //
    // These instructions would be processed as follows:
    //    Because a starts at 0, it is not greater than 1, and so b is not modified.
    //    a is increased by 1 (to 1) because b is less than 5 (it is 0).
    //    c is decreased by -10 (to 10) because a is now greater than or equal to 1 (it is 1).
    //    c is increased by -20 (to -10) because c is equal to 10.
    //
    // After this process, the largest value in any register is 1.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(1, testRawInput))

    //
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(10, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(3_089, 5_391))
}
