package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day18Test :
    AoCEmptyTest<Day18, Int>(),
    IAoCDoubleTest<Day18, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day18()
    }

    // At the time the recover operation is executed, the frequency of the last sound played is 4.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(4, testRawInput))

    // Part two runs a different sample program, in which each copy sends three values before both
    // deadlock on their fourth rcv, so program 1 sent 3 values.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                3,
                listOf("snd 1", "snd 2", "snd p", "rcv a", "rcv b", "rcv c", "rcv d"),
            ),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(8_600, 7_239))
}
