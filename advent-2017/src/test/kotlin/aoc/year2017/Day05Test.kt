package aoc.year2017

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day05Test : AoCDoubleTest<Day05, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day05()
    }

    // The following steps would be taken before an exit is found:
    //    (0) 3  0  1  -3  - before we have taken any steps.
    //    (1) 3  0  1  -3  - jump with offset 0 (that is, don't jump at all). Fortunately, the instruction is
    //          then incremented to 1.
    //     2 (3) 0  1  -3  - step forward because of the instruction we just modified. The first instruction
    //          is incremented again, now to 2.
    //     2  4  0  1 (-3) - jump all the way to the end; leave a 4 behind.
    //     2 (4) 0  1  -2  - go back to where we just were; increment -3 to -2.
    //     2  5  0  1  -2  - jump 4 steps forward, escaping the maze.
    //
    // In this example, the exit is reached in 5 steps.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(5, testRawInput))

    // Now, the jumps are even stranger: after each jump, if the offset was three or more, instead decrease
    // it by 1. Otherwise, increase it by 1 as before.
    //
    // Using this rule with the above example, the process now takes 10 steps, and the offset values after
    // finding the exit are left as 2 3 2 3 -1.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(10, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(351_282, 24_568_703))
}
