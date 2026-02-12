package aoc.year2016

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day16Test : AoCSingleFunctionTest<Day16, String, Int, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day16()
        partOneFunc = currentDay::solve
    }

    // Combining all of these steps together, suppose you want to fill a disk of length 20 using an initial
    // state of 10000:
    //    Because 10000 is too short, we first use the modified dragon curve to make it longer.
    //    After one round, it becomes 10000011110 (11 characters), still too short.
    //    After two rounds, it becomes 10000011110010000111110 (23 characters), which is enough.
    //    Since we only need 20, but we have 23, we get rid of all but the first 20 characters: 10000011110010000111.
    //    Next, we start calculating the checksum; after one round, we have 0111110101, which 10 characters long
    //      (even), so we continue.
    //    After two rounds, we have 01100, which is 5 characters long (odd), so we are done.
    //
    // In this example, the correct checksum would therefore be 01100.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("01100", "10000", 20))

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of("11100111011101111", "10001110010000110"),
        )
}
