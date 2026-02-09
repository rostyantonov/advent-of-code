package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day07Test : AoCDoubleTest<Day07, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day07()
    }

    // For example:
    //     abba[mnop]qrst supports TLS (abba outside square brackets).
    //     abcd[bddb]xyyx does not support TLS (bddb is within square brackets,
    //          even though xyyx is outside square brackets).
    //     aaaa[qwer]tyui does not support TLS (aaaa is invalid; the interior characters must be different).
    //     ioxxoj[asdfgh]zxcvbn supports TLS (oxxo is outside square brackets, even though it's within a larger string)
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(2, testRawInput))

    // For example:
    //    aba[bab]xyz supports SSL (aba outside square brackets with corresponding bab within square brackets).
    //    xyx[xyx]xyx does not support SSL (xyx, but no corresponding yxy).
    //    aaa[kek]eke supports SSL (eke in supernet with corresponding kek in hypernet;
    //      the aaa sequence is not related, because the interior character must be different).
    //    zazbz[bzb]cdb supports SSL (zaz has no corresponding aza, but zbz has a corresponding bzb,
    //      even though zaz and zbz overlap).
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(
                3,
                listOf(
                    "aba[bab]xyz",
                    "xyx[xyx]xyx",
                    "aaa[kek]eke",
                    "zazbz[bzb]cdb",
                ),
            ),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(118, 260))
}
