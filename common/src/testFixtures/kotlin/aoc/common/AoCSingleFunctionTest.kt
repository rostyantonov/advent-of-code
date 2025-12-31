package aoc.common

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

abstract class AoCSingleFunctionTest<DayType : IAoCDay<Result>, Result, FuncIn, FuncResult> :
    AoCEmptyTest<DayType, Result>(),
    IAoCSingleTest {
    lateinit var partOneFunc: (FuncIn) -> FuncResult

    @ParameterizedTest(name = "Checking test one inputs n: {index}")
    @MethodSource("partOneInput")
    fun taskOneTest(
        funcResult: FuncResult,
        dayInput: Any,
        funcInput: FuncIn,
    ) {
        currentDay.rawInput = getRawInput(dayInput)
        assertEquals(funcResult, partOneFunc(funcInput))
    }
}
