package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

interface IAoCSingleTest<DayType : IAoCDay<Result>, Result : Any> : IAoCEmptyTest<DayType, Result> {
    fun partOneInput(): Stream<Arguments>

    @ParameterizedTest(name = "Checking test one inputs n: {index}")
    @MethodSource("partOneInput")
    fun partOneTest(
        dayResult: Any,
        dayInput: Any,
    ) = assertEquals(dayResult, currentDay.apply { this.rawInput = getRawInput(dayInput) }.processPartOne())
}
