package aoc.year2016

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day08Test : AoCSingleFunctionTest<Day08, String, Pair<Int, Int>, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day08()
        partOneFunc = currentDay::doPartOne
    }

    // For example, here is a simple sequence on a smaller screen:
    //  rect 3x2 creates a small rectangle in the top-left corner:
    //      ###....
    //      ###....
    //      .......
    //  rotate column x=1 by 1 rotates the second column down by one pixel:
    //      #.#....
    //      ###....
    //      .#.....
    //  rotate row y=0 by 4 rotates the top row right by four pixels:
    //    ....#.#
    //    ###....
    //    .#.....
    //  rotate column x=1 by 1 again rotates the second column down by one pixel,
    //      causing the bottom pixel to wrap back to the top:
    //    .#..#.#
    //    #.#....
    //    .#.....
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("6", testRawInput, Pair(7, 3)))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("119", "ZFHFSFOGPO"))
}
