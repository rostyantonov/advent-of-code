package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day05Test : AoCDoubleTest<Day05, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day05()
    }

    // The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing
    //      abc3231929; the sixth character of the hash, and thus the first character of the password, is 1.
    //  5017308 produces the next interesting hash, which starts with 000008f82...,
    //      so the second character of the password is 8.
    // The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
    //
    // In this example, after continuing this search a total of eight times, the password is 18f47a30.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("18f47a30", "abc"))

    // The first interesting hash is from abc3231929, which produces 0000015...;
    //      so, 5 goes in position 1: _5______.
    // In the previous method, 5017308 produced an interesting hash;
    //      however, it is ignored, because it specifies an invalid position (8).
    // The second interesting hash is at index 5357525, which produces 000004e...;
    //      so, e goes in position 4: _5__e___.
    //
    // You almost choke on your popcorn as the final character falls into place, producing the password 05ace8e3.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of("05ace8e3", "abc"))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("d4cd2ee1", "f2c730e5"))
}
