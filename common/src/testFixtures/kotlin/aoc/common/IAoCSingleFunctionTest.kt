package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

interface IAoCSingleFunctionTest<DayType : IAoCDay<Result>, Result : Any, FuncIn, FuncResult : Any> :
    IAoCEmptyTest<DayType, Result> {
    fun partOneFunctionInput(): Stream<Arguments>

    val partOneFunc: (FuncIn) -> FuncResult

    @ParameterizedTest(name = "Checking test one inputs n: {index}")
    @MethodSource("partOneFunctionInput")
    fun taskOneFunctionTest(
        funcResult: Any,
        dayInput: Any,
        funcInput: FuncIn,
    ) {
        currentDay.rawInput = getRawInput(dayInput)
        assertEquals<Any>(funcResult, partOneFunc(funcInput))
    }
}
