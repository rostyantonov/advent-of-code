package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day03Test : AoCDoubleTest<Day03, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day03()
    }

    // > delivers presents to 2 houses: one at the starting location, and one to the east.
    // ^>v< delivers presents to 4 houses in a square, including twice to the house at his starting/ending location.
    // ^v^v^v^v^v delivers a bunch of presents to some very lucky children at only 2 houses.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(2, ">"),
            Arguments.of(4, "^>v<"),
            Arguments.of(2, "^v^v^v^v^v"),
        )

    // ^v delivers presents to 3 houses, because Santa goes north, and then Robo-Santa goes south.
    // ^>v< now delivers presents to 3 houses, and Santa and Robo-Santa end up back where they started.
    // ^v^v^v^v^v now delivers presents to 11 houses, with Santa going one direction and Robo-Santa going the other.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, "^v"),
            Arguments.of(3, "^>v<"),
            Arguments.of(11, "^v^v^v^v^v"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(2_572, 2_631))
}
