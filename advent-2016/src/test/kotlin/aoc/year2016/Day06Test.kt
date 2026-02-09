package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day06Test : AoCDoubleTest<Day06, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day06()
    }

    // For example, suppose you had recorded the following messages:
    //      eedadn
    //      drvtee
    //      eandsr
    //      raavrd
    //      atevrs
    //      tsrnev
    //      sdttsa
    //      rasrtv
    //      nssdts
    //      ntnada
    //      svetve
    //      tesnvt
    //      vntsnd
    //      vrdear
    //      dvrsen
    //      enarar
    //
    // The most common character in the first column is e; in the second, a; in the third, s, and so on.
    // Combining these characters returns the error-corrected message, easter.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("easter", testRawInput))

    // In the above example, the least common character in the first column is a; in the second, d, and so on.
    // Repeating this process for the remaining characters produces the original message, advent.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of("advent", testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("xdkzukcf", "cevsgyvd"))
}
