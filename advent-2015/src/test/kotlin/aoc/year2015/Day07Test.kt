package aoc.year2015

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day07Test : AoCSingleFunctionTest<Day07, Int, String, UShort>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day07()
        partOneFunc = { value ->
            currentDay.loadConnectionsMap()
            currentDay.getCalculated(value)
        }
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
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of((72u).toUShort(), testRawInput, "d"),
            Arguments.of((507u).toUShort(), testRawInput, "e"),
            Arguments.of((492u).toUShort(), testRawInput, "f"),
            Arguments.of((114u).toUShort(), testRawInput, "g"),
            Arguments.of((65_412u).toUShort(), testRawInput, "h"),
            Arguments.of((65_079u).toUShort(), testRawInput, "i"),
            Arguments.of((123u).toUShort(), testRawInput, "x"),
            Arguments.of((456u).toUShort(), testRawInput, "y"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(3_176, 14_710))
}
