package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day10Test :
    AoCEmptyTest<Day10, String>(),
    IAoCSingleFunctionTest<Day10, String, Int, Int>,
    IAoCDoubleTest<Day10, String> {
    override val partOneFunc: (Int) -> Int
        get() = currentDay::solvePartOne

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day10()
    }

    // Suppose we instead only had a circular list containing five elements, 0, 1, 2, 3, 4, and were
    // given input lengths of 3, 4, 1, 5.
    override fun partOneFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(12, "3,4,1,5", 5),
        )

    // Do Nothing as first part is function test
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("0", "0"),
        )

    // Here are some example hashes:
    //
    //    The empty string becomes a2582a3a0e66e6e86e3812dcb672a272.
    //    AoC 2017 becomes 33efeb34ea91902bb2f59c9920caa6cd.
    //    1,2,3 becomes 3efbe78a8d82f29979031a4aa0b16a9d.
    //    1,2,4 becomes 63960835bcdc130f0b66d7ff4f6a5a8e.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("a2582a3a0e66e6e86e3812dcb672a272", ""),
            Arguments.of("33efeb34ea91902bb2f59c9920caa6cd", "AoC 2017"),
            Arguments.of("3efbe78a8d82f29979031a4aa0b16a9d", "1,2,3"),
            Arguments.of("63960835bcdc130f0b66d7ff4f6a5a8e", "1,2,4"),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                "11413",
                "7adfd64c2a03a4968cf708d1b7fd418d",
            ),
        )
}
