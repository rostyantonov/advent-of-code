package aoc.common

import aoc.common.input.FileInputDelegate
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TimingExtension::class)
abstract class AoCEmptyTest<DayType : IAoCDay<Result>, Result : Any> : IAoCEmptyTest<DayType, Result> {
    override lateinit var currentDay: DayType

    override val testRawInput: List<String> by FileInputDelegate()
}
