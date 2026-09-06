package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

interface IAoCDoubleTest<DayType : IAoCDay<Result>, Result : Any> : IAoCSingleTest<DayType, Result> {
    fun partTwoInput(): Stream<Arguments>

    @ParameterizedTest(name = "Checking test two inputs n: {index}")
    @MethodSource("partTwoInput")
    fun partTwoTest(
        dayResult: Any,
        dayInput: Any,
    ) = assertEquals(dayResult, currentDay.apply { this.rawInput = getRawInput(dayInput) }.processPartTwo())
}
