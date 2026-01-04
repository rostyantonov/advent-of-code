package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

abstract class AoCSingleTest<DayType : IAoCDay<Result>, Result> :
    AoCEmptyTest<DayType, Result>(),
    IAoCSingleTest {
    @ParameterizedTest(name = "Checking test one inputs n: {index}")
    @MethodSource("partOneInput")
    fun partOneTest(
        dayResult: Result,
        dayInput: Any,
    ) = assertEquals(dayResult, currentDay.apply { this.rawInput = getRawInput(dayInput) }.processPartOne())
}
