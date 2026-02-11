package aoc.year2016

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day12Test : AoCSingleTest<Day12, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day12()
    }

    // For example:
    //      cpy 41 a
    //      inc a
    //      inc a
    //      dec a
    //      jnz a 2
    //      dec a
    //
    // The above code would set register a to 41, increase its value by 2, decrease its value by 1,
    // and then skip the last dec a (because a is not zero, so the jnz a 2 skips it), leaving register a at 42.
    // When you move past the last instruction, the program halts.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(42, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(318_003, 9_227_657))
}
