package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day12Test :
    AoCEmptyTest<Day12, Int>(),
    IAoCDoubleTest<Day12, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day12()
    }

    // For example, suppose you go door-to-door like a travelling salesman and record the following list:
    //      0 <-> 2
    //      1 <-> 1
    //      2 <-> 0, 3, 4
    //      3 <-> 2, 4
    //      4 <-> 2, 3, 6
    //      5 <-> 6
    //      6 <-> 4, 5
    //
    // In this example, the following programs are in the group that contains program ID 0:
    //    Program 0 by definition.
    //    Program 2, directly connected to program 0.
    //    Program 3 via program 2.
    //    Program 4 via program 2.
    //    Program 5 via programs 6, then 4, then 2.
    //    Program 6 via programs 4, then 2.
    //
    // Therefore, a total of 6 programs are in this group
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(6, testRawInput))

    // In the example above, there were 2 groups: one consisting of programs 0,2,3,4,5,6, and the other
    // consisting solely of program 1.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(2, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(378, 204))
}
