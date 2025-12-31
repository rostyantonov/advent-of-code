package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day06Test : AoCDoubleTest<Day06, Int>() {
    private val taskOneLineOne = "turn on 0,0 through 999,999"
    private val taskOneLineTwo = "toggle 0,0 through 999,0"
    private val taskOneLineThree = "turn off 499,499 through 500,500"
    private val taskTwoLineOne = "turn on 0,0 through 0,0"
    private val taskTwoLineTwo = "toggle 0,0 through 999,999"

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day06()
    }

    // turn on 0,0 through 999,999 would turn on (or leave on) every light.
    // toggle 0,0 through 999,0 would toggle the first line of 1000 lights,
    //   turning off the ones that were on,
    //   and turning on the ones that were off.
    // turn off 499,499 through 500,500
    //   would turn off (or leave off) the middle four lights.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1_000_000, taskOneLineOne),
            Arguments.of(1_000, taskOneLineTwo),
            Arguments.of(0, taskOneLineThree),
            Arguments.of(999_000, listOf(taskOneLineOne, taskOneLineTwo)),
            Arguments.of(999_996, listOf(taskOneLineOne, taskOneLineThree)),
            Arguments.of(1_000, listOf(taskOneLineTwo, taskOneLineThree)),
            Arguments.of(998_996, listOf(taskOneLineOne, taskOneLineTwo, taskOneLineThree)),
        )

    // turn on 0,0 through 0,0 would increase the total brightness by 1.
    // toggle 0,0 through 999,999 would increase the total brightness by 2000000.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1, taskTwoLineOne),
            Arguments.of(2_000_000, taskTwoLineTwo),
            Arguments.of(2_000_001, listOf(taskTwoLineOne, taskTwoLineTwo)),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(400_410, 15_343_601))
}
