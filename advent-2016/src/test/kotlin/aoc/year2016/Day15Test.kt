package aoc.year2016

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day15Test : AoCSingleTest<Day15, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day15()
    }

    // For example, at time=0, suppose you see the following arrangement:
    //      Disc #1 has 5 positions; at time=0, it is at position 4.
    //      Disc #2 has 2 positions; at time=0, it is at position 1.
    //
    // If you press the button exactly at time=0, the capsule would start to fall; it would reach the first disc
    // at time=1. Since the first disc was at position 4 at time=0, by time=1 it has ticked one position forward.
    // As a five-position disc, the next position is 0, and the capsule falls through the slot.
    //
    // Then, at time=2, the capsule reaches the second disc. The second disc has ticked forward two positions at
    // this point: it started at position 1, then continued to position 0, and finally ended up at position 1 again.
    // Because there's only a slot at position 0, the capsule bounces away.
    //
    // If, however, you wait until time=5 to push the button, then when the capsule reaches each disc, the first disc
    // will have ticked forward 5+1 = 6 times (to position 0), and the second disc will have ticked forward 5+2 = 7
    // times (also to position 0). In this case, the capsule would fall through the discs and come out of the machine.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(5, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(203_660, 2_408_135))
}
