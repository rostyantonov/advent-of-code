package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

abstract class AoCDoubleFunctionTest<DayType : IAoCDay<Result>, Result, FuncIn, FuncResult> :
    AoCSingleFunctionTest<DayType, Result, FuncIn, FuncResult>(),
    IAoCDoubleTest {
    lateinit var partTwoFunc: (FuncIn) -> FuncResult

    @ParameterizedTest(name = "Checking test two inputs n: {index}")
    @MethodSource("partTwoInput")
    fun taskTwoTest(
        funcResult: FuncResult,
        dayInput: Any,
        funcInput: FuncIn,
    ) {
        currentDay.rawInput = getRawInput(dayInput)
        assertEquals(funcResult, partTwoFunc(funcInput))
    }
}
