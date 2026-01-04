package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

abstract class AoCDoubleTest<DayType : IAoCDay<Result>, Result> :
    AoCSingleTest<DayType, Result>(),
    IAoCDoubleTest {
    @ParameterizedTest(name = "Checking test two inputs n: {index}")
    @MethodSource("partTwoInput")
    fun partTwoTest(
        dayResult: Result,
        dayInput: Any,
    ) = assertEquals(dayResult, currentDay.apply { this.rawInput = getRawInput(dayInput) }.processPartTwo())
}
