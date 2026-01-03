package aoc.year2015

import aoc.common.AoCEmptyTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day20Test : AoCEmptyTest<Day20, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day20()
    }

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(831_600, 884_520))
}
