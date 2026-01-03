package aoc.year2015

import aoc.common.AoCSingleTest
import aoc.common.input.FileListInputDelegate
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day19Test : AoCSingleTest<Day19, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day19()
    }

    private val testInput by FileListInputDelegate(testFilesAmount = 2)

    // For example, imagine a simpler machine that supports only the following replacements:
    // H => HO
    // H => OH
    // O => HH
    //
    // Given the replacements above and starting with HOH, the following molecules could be generated:
    //    HOOH (via H => HO on the first H).
    //    HOHO (via H => HO on the second H).
    //    OHOH (via H => OH on the first H).
    //    HOOH (via H => OH on the second H).
    //    HHHH (via O => HH).
    //
    // So, in the example above, there are 4 distinct molecules (not five, because HOOH appears twice)
    // after one replacement from HOH. Santa's favorite molecule, HOHOHO, can become 7 distinct molecules
    // (over nine replacements: six from H, and three from O).
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(4, testInput[0]),
            Arguments.of(7, testInput[1]),
        )

    // Due optimizations specific for task input, there is not possible to use original algorithm,
    // so tests will be useless

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(509, 195))
}
