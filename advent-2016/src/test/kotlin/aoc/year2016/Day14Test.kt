package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day14Test : AoCDoubleTest<Day14, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day14()
    }

    // For example, if the pre-arranged salt is abc:
    // The first index which produces a triple is 18, because the MD5 hash of abc18 contains ...cc38887a5....
    //    However, index 18 does not count as a key for your one-time pad, because none of the next thousand
    //    hashes (index 19 through index 1018) contain 88888.
    // The next index which produces a triple is 39; the hash of abc39 contains eee. It is also the first key:
    //    one of the next thousand hashes (the one at index 816) contains eeeee.
    // None of the next six triples are keys, but the one after that, at index 92, is: it contains 999 and
    //    index 200 contains 99999.
    // Eventually, index 22728 meets all of the criteria to generate the 64th key.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(22_728, "abc"))

    // Again for salt abc:
    // The first triple (222, at index 5) has no matching 22222 in the next thousand hashes.
    // The second triple (eee, at index 10) hash a matching eeeee at index 89, and so it is the first key.
    // Eventually, index 22551 produces the 64th key (triple fff with matching fffff at index 22859.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(22_551, "abc"))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(35_186, 22_429))
}
