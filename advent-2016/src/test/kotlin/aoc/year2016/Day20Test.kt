package aoc.year2016

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day20Test : AoCSingleTest<Day20, Long>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day20()
    }

    // Example from problem:
    // Blacklist: 5-8, 0-2, 4-7
    // Valid IPs (0-9): 3 and 9
    // Part 1: Lowest valid IP = 3
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(3L, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(17_348_574L, 104L))
}
