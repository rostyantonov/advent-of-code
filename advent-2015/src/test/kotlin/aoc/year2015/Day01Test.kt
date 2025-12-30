package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day01Test : AoCDoubleTest<Day01, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day01()
    }

    // (()) and ()() both result in floor 0.
    // ((( and (()(()( both result in floor 3.
    // ))((((( also results in floor 3.
    // ()) and ))( both result in floor -1 (the first basement level).
    // ))) and )())()) both result in floor -3.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(0, "(())"),
            Arguments.of(0, "()()"),
            Arguments.of(3, "((("),
            Arguments.of(3, "(()(()("),
            Arguments.of(3, "))((((("),
            Arguments.of(-1, "())"),
            Arguments.of(-1, "))("),
            Arguments.of(-3, ")))"),
            Arguments.of(-3, ")())())"),
        )

    // ) causes him to enter the basement at character position 1.
    // ()()) causes him to enter the basement at character position 5.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1, ")"),
            Arguments.of(5, "()())"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(280, 1_797))
}
