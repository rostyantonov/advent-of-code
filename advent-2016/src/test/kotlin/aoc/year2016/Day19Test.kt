package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day19Test : AoCDoubleTest<Day19, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day19()
    }

    // For example, with five Elves (numbered 1 to 5):
    //
    //  1
    // 5   2
    // 4 3
    //
    //    Elf 1 takes Elf 2's present.
    //    Elf 2 has no presents and is skipped.
    //    Elf 3 takes Elf 4's present.
    //    Elf 4 has no presents and is also skipped.
    //    Elf 5 takes Elf 1's two presents.
    //    Neither Elf 1 nor Elf 2 have any presents, so both are skipped.
    //    Elf 3 takes Elf 5's three presents.
    //
    // So, with five Elves, the Elf that sits starting in position 3 gets all the presents.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(3, 5))

    // For example, with five Elves (again numbered 1 to 5):
    // So, with five Elves, the Elf that sits starting in position 2 gets all the presents.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(2, 5))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_834_471, 1_420_064))
}
