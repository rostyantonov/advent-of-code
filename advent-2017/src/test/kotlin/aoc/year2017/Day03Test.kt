package aoc.year2017

import aoc.common.AoCDoubleTest
import aoc.common.input.FileListInputDelegate
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day03Test : AoCDoubleTest<Day03, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day03()
    }

    private val testInput by FileListInputDelegate(testFilesAmount = 2)

    // For example:
    //    Data from square 1 is carried 0 steps, since it's at the access port.
    //    Data from square 12 is carried 3 steps, such as: down, left, left.
    //    Data from square 23 is carried only 2 steps: up twice.
    //    Data from square 1024 must be carried 31 steps.
    //
    // In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(0, 1),
            Arguments.of(3, 12),
            Arguments.of(2, 23),
            Arguments.of(31, 1024),
        )

    // Once a square is written, its value does not change. Therefore, the first few squares would receive
    // the following values:
    //   147  142  133  122   59
    //   304    5    4    2   57
    //   330   10    1    1   54
    //   351   11   23   25   26
    //   362  747  806--->   ...
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(2, 1),
            Arguments.of(4, 2),
            Arguments.of(5, 4),
            Arguments.of(10, 5),
            Arguments.of(11, 10),
            Arguments.of(23, 11),
            Arguments.of(25, 23),
            Arguments.of(26, 25),
            Arguments.of(54, 26),
            Arguments.of(57, 54),
            Arguments.of(59, 57),
            Arguments.of(122, 59),
            Arguments.of(133, 122),
            Arguments.of(142, 133),
            Arguments.of(147, 142),
            Arguments.of(304, 147),
            Arguments.of(330, 304),
            Arguments.of(351, 330),
            Arguments.of(362, 351),
            Arguments.of(747, 362),
            Arguments.of(806, 747),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(430, 312_453))
}
