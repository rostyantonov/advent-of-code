package aoc.year2015

import aoc.common.AoCEmptyTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day16Test : AoCEmptyTest<Day16, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day16()
    }

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(103, 405))
}
