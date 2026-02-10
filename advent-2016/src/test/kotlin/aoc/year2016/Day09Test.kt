package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day09Test : AoCDoubleTest<Day09, Long>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day09()
    }

    // For example:
    //    ADVENT contains no markers and decompresses to itself with no changes,
    //          resulting in a decompressed length of 6.
    //    A(1x5)BC repeats only the B a total of 5 times, becoming ABBBBBC for a decompressed length of 7.
    //    (3x3)XYZ becomes XYZXYZXYZ for a decompressed length of 9.
    //    A(2x2)BCD(2x2)EFG doubles the BC and EF, becoming ABCBCDEFEFG for a decompressed length of 11.
    //    (6x1)(1x3)A simply becomes (1x3)A - the (1x3) looks like a marker, but because it's within a data
    //          section of another marker, it is not treated any differently from the A that comes after it.
    //          It has a decompressed length of 6.
    //    X(8x2)(3x3)ABCY becomes X(3x3)ABC(3x3)ABCY (for a decompressed length of 18), because the decompressed
    //          data from the (8x2) marker (the (3x3)ABC) is skipped and not processed further.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6L, "ADVENT"),
            Arguments.of(7L, "A(1x5)BC"),
            Arguments.of(9L, "(3x3)XYZ"),
            Arguments.of(11L, "A(2x2)BCD(2x2)EFG"),
            Arguments.of(6L, "(6x1)(1x3)A"),
            Arguments.of(18L, "X(8x2)(3x3)ABCY"),
        )

    // For example:
    //    (3x3)XYZ still becomes XYZXYZXYZ, as the decompressed section contains no markers.
    //    X(8x2)(3x3)ABCY becomes XABCABCABCABCABCABCY, because the decompressed data from the (8x2) marker is
    //          then further decompressed, thus triggering the (3x3) marker twice for a total of six ABC sequences.
    //    (27x12)(20x12)(13x14)(7x10)(1x12)A decompresses into a string of A repeated 241920 times.
    //    (25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN becomes 445 characters long.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(9L, "(3x3)XYZ"),
            Arguments.of(20L, "X(8x2)(3x3)ABCY"),
            Arguments.of(241_920L, "(27x12)(20x12)(13x14)(7x10)(1x12)A"),
            Arguments.of(445L, "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(183_269L, 11_317_278_863L))
}
