package aoc.year2016

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day10Test :
    AoCEmptyTest<Day10, Int>(),
    IAoCSingleFunctionTest<Day10, Int, Pair<Int, Int>, Int> {
    override val partOneFunc: (Pair<Int, Int>) -> Int
        get() = currentDay::findBot

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day10()
    }

    // For example, consider the following instructions:
    //      value 5 goes to bot 2
    //      bot 2 gives low to bot 1 and high to bot 0
    //      value 3 goes to bot 1
    //      bot 1 gives low to output 1 and high to bot 0
    //      bot 0 gives low to output 2 and high to output 0
    //      value 2 goes to bot 2
    //
    //      Initially, bot 1 starts with a value-3 chip, and bot 2 starts with a value-2 chip and a value-5 chip.
    //      Because bot 2 has two microchips, it gives its lower one (2) to bot 1 and its higher one (5) to bot 0.
    //      Then, bot 1 has two microchips; it puts the value-2 chip in output 1 and gives the value-3 chip to bot 0.
    //      Finally, bot 0 has two microchips; it puts the 3 in output 2 and the 5 in output 0.
    //
    // In the end, output bin 0 contains a value-5 microchip, output bin 1 contains a value-2 microchip,
    // and output bin 2 contains a value-3 microchip. In this configuration, bot number 2 is responsible for
    // comparing value-5 microchips with value-2 microchips.
    override fun partOneFunctionInput(): Stream<Arguments> = Stream.of(Arguments.of(2, testRawInput, Pair(2, 5)))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(157, 1085))
}
