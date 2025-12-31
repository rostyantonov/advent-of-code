package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

interface IAoCDoubleFunctionTest<DayType : IAoCDay<Result>, Result : Any, FuncIn, FuncResult : Any> :
    IAoCSingleFunctionTest<DayType, Result, FuncIn, FuncResult> {
    fun partTwoFunctionInput(): Stream<Arguments>

    val partTwoFunc: (FuncIn) -> FuncResult

    @ParameterizedTest(name = "Checking test two inputs n: {index}")
    @MethodSource("partTwoFunctionInput")
    fun taskTwoFunctionTest(
        funcResult: Any,
        dayInput: Any,
        funcInput: FuncIn,
    ) {
        currentDay.rawInput = getRawInput(dayInput)
        assertEquals<Any>(funcResult, partTwoFunc(funcInput))
    }
}
