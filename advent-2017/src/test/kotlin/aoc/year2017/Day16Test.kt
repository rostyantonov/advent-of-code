package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day16Test :
    AoCEmptyTest<Day16, String>(),
    IAoCSingleFunctionTest<Day16, String, Int, String> {
    override val partOneFunc: (Int) -> String
        get() = currentDay::makeADance

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day16()
    }

    // For example, with only five programs standing in a line (abcde), they could do the following dance:
    //    s1, a spin of size 1: eabcd.
    //    x3/4, swapping the last two programs: eabdc.
    //    pe/b, swapping programs e and b: baedc.
    //
    // After finishing their dance, the programs end up in order baedc.
    override fun partOneFunctionInput(): Stream<Arguments> = Stream.of(Arguments.of("baedc", testRawInput, 5))

    //
//    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of("21", testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("pkgnhomelfdibjac", "pogbjfihclkemadn"))
}
