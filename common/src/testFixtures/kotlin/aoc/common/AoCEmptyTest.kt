package aoc.common

import aoc.common.input.FileInputDelegate
import aoc.common.input.IAoCFileInput
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TimingExtension::class)
abstract class AoCEmptyTest<DayType : IAoCDay<Result>, Result> : IAoCFileInput {
    lateinit var currentDay: DayType

    val testRawInput: List<String> by FileInputDelegate()

    // implementation needs to be annotated with @BeforeTest
    // it is called before each test to guarantee a new clean object
    abstract fun setupCurrentDay()

    @ParameterizedTest(name = "Checking real results")
    @MethodSource("realResults")
    fun checkRealResults(
        partOneResult: Result,
        partTwoResult: Result,
    ) {
        assertEquals(partOneResult, currentDay.processPartOne())
        assertEquals(partTwoResult, currentDay.processPartTwo())
    }

    abstract fun realResults(): Stream<Arguments>

    fun getRawInput(dayInput: Any): List<String> =
        when (dayInput) {
            is String -> listOf(dayInput)
            is List<*> -> dayInput.map { it.toString() }
            else -> emptyList()
        }
}
