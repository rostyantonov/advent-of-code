package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day11Test :
    AoCEmptyTest<Day11, Int>(),
    IAoCSingleTest<Day11, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day11()
    }

    // For example:
    //
    //    ne,ne,ne is 3 steps away.
    //    ne,ne,sw,sw is 0 steps away (back where you started).
    //    ne,ne,s,s is 2 steps away (se,se).
    //    se,sw,se,sw,sw is 3 steps away (s,s,sw).
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, "ne,ne,ne"),
            Arguments.of(0, "ne,ne,sw,sw"),
            Arguments.of(2, "ne,ne,s,s"),
            Arguments.of(3, "se,sw,se,sw,sw"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(834, 1_569))
}
