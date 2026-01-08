package aoc.year2015

import aoc.common.AoCEmptyTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day25Test : AoCEmptyTest<Day25, Long>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day25()
    }

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(8_997_277L, -1L))
}
