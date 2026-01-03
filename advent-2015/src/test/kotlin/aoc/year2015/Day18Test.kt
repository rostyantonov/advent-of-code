package aoc.year2015

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day18Test :
    AoCEmptyTest<Day18, Int>(),
    IAoCDoubleFunctionTest<Day18, Int, Int, Int> {
    override val partOneFunc: (Int) -> Int
        get() = currentDay::checkPartOne

    override val partTwoFunc: (Int) -> Int
        get() = currentDay::checkPartTwo

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day18()
    }

    // Initial state:
    // .#.#.#
    // ...##.
    // #....#
    // ..#...
    // #.#..#
    // ####..

    // After 4 steps:
    // ......
    // ......
    // ..##..
    // ..##..
    // ......
    // ......
    // After 4 steps, this example has four lights on.

    override fun partOneFunctionInput(): Stream<Arguments> = Stream.of(Arguments.of(4, testRawInput, 4))

    // After 5 steps:
    // ##.###
    // .##..#
    // .##...
    // .##...
    // #.#...
    // ##...#
    // After 5 steps, this example now has 17 lights on.

    override fun partTwoFunctionInput(): Stream<Arguments> = Stream.of(Arguments.of(17, testRawInput, 5))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(1_061, 1_006))
}
