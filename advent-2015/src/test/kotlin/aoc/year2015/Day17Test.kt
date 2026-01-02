package aoc.year2015

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day17Test :
    AoCEmptyTest<Day17, Int>(),
    IAoCDoubleFunctionTest<Day17, Int, Int, Int> {
    override val partOneFunc: (Int) -> Int
        get() = currentDay::checkPartOne

    override val partTwoFunc: (Int) -> Int
        get() = currentDay::checkPartTwo

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day17()
    }

    // For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters.
    // If you need to store 25 liters, there are four ways to do it:
    //
    //    15 and 10
    //    20 and 5 (the first 5)
    //    20 and 5 (the second 5)
    //    15, 5, and 5
    override fun partOneFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(4, listOf(20, 15, 10, 5, 5), 25),
        )

    // In the example above, the minimum number of containers was two.
    // There were three ways to use that many containers, and so the answer there would be 3.
    override fun partTwoFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(3, listOf(20, 15, 10, 5, 5), 25),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_304, 18))
}
