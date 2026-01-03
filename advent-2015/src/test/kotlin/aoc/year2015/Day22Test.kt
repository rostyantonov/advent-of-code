package aoc.year2015

import aoc.common.AoCEmptyTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day22Test : AoCEmptyTest<Day22, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day22()
    }

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(900, 1_216))
}
