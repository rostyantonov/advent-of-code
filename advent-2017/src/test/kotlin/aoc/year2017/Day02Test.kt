package aoc.year2017

import aoc.common.AoCDoubleTest
import aoc.common.input.FileListInputDelegate
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day02Test : AoCDoubleTest<Day02, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day02()
    }

    private val testInput by FileListInputDelegate(testFilesAmount = 2)

    // For example, given the following spreadsheet:
    //    The first row's largest and smallest values are 9 and 1, and their difference is 8.
    //    The second row's largest and smallest values are 7 and 3, and their difference is 4.
    //    The third row's difference is 6.
    //
    // In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(18, testInput[0]))

    // For example, given the following spreadsheet:
    //    In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
    //    In the second row, the two numbers are 9 and 3; the result is 3.
    //    In the third row, the result is 2.
    //
    // In this example, the sum of the results would be 4 + 3 + 2 = 9.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(9, testInput[1]))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(41_887, 226))
}
