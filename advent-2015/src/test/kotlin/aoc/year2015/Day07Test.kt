package aoc.year2015

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day07Test :
    AoCEmptyTest<Day07, Int>(),
    IAoCSingleFunctionTest<Day07, Int, String, Int> {
    override val partOneFunc: (String) -> Int
        get() = { value ->
            currentDay.loadConnectionsMap()
            currentDay.getCalculated(value).toInt()
        }

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day07()
    }

    // 123 -> x
    // 456 -> y
    // x AND y -> d
    // x OR y -> e
    // x LSHIFT 2 -> f
    // y RSHIFT 2 -> g
    // NOT x -> h
    // NOT y -> i

    // After it is run, these are the signals on the wires:

    // d: 72
    // e: 507
    // f: 492
    // g: 114
    // h: 65412
    // i: 65079
    // x: 123
    // y: 456
    override fun partOneFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(72, testRawInput, "d"),
            Arguments.of(507, testRawInput, "e"),
            Arguments.of(492, testRawInput, "f"),
            Arguments.of(114, testRawInput, "g"),
            Arguments.of(65_412, testRawInput, "h"),
            Arguments.of(65_079, testRawInput, "i"),
            Arguments.of(123, testRawInput, "x"),
            Arguments.of(456, testRawInput, "y"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(3_176, 14_710))
}
