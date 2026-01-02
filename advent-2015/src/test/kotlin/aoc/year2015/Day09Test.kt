package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day09Test : AoCDoubleTest<Day09, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day09()
    }

    // London to Dublin = 464
    // London to Belfast = 518
    // Dublin to Belfast = 141

    // The possible routes are therefore:

    // Dublin -> London -> Belfast = 982
    // London -> Dublin -> Belfast = 605
    // London -> Belfast -> Dublin = 659
    // Dublin -> Belfast -> London = 659
    // Belfast -> Dublin -> London = 605
    // Belfast -> London -> Dublin = 982

    // Shortest path
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(605, testRawInput))

    // Longest path
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(982, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(141, 736))
}
