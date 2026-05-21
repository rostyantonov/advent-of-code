package aoc.year2016

import aoc.common.AoCEmptyTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day25Test : AoCEmptyTest<Day25, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day25()
    }

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(182, -1))
}
