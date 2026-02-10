package aoc.year2016

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day11Test : AoCSingleTest<Day11, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day11()
    }

    /**
     * For example, suppose the isolated area has the following arrangement:
     *
     *      The first floor contains a hydrogen-compatible microchip and a lithium-compatible microchip.
     *      The second floor contains a hydrogen generator.
     *      The third floor contains a lithium generator.
     *      The fourth floor contains nothing relevant.
     *
     * As a diagram (F# for a Floor number, E for Elevator, H for Hydrogen, L for Lithium, M for Microchip,
     * and G for Generator), the initial state looks like this:
     *
     *      F4 .  .  .  .  .
     *      F3 .  .  .  LG .
     *      F2 .  HG .  .  .
     *      F1 E  .  HM .  LM
     *
     * In this arrangement, it takes 11 steps to collect all of the objects at the fourth floor for assembly.
     * (Each elevator stop counts as one step, even if nothing is added to or removed from it.)
     */
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(11, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(33, 57))
}
