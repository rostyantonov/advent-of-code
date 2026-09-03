package aoc.year2017

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day04Test : AoCDoubleTest<Day04, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day04()
    }

    // For example:
    //    aa bb cc dd ee is valid.
    //    aa bb cc dd aa is not valid - the word aa appears more than once.
    //    aa bb cc dd aaa is valid - aa and aaa count as different words.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1, "aa bb cc dd ee"),
            Arguments.of(0, "aa bb cc dd aa"),
            Arguments.of(1, "aa bb cc dd aaa"),
        )

    // For example:
    //    abcde fghij is a valid passphrase.
    //    abcde xyz ecdab is not valid - the letters from the third word can be rearranged to form the first word.
    //    a ab abc abd abf abj is a valid passphrase, because all letters need to be used when forming another word.
    //    iiii oiii ooii oooi oooo is valid.
    //    oiii ioii iioi iiio is not valid - any of these words can be rearranged to form any other word.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1, "abcde fghij"),
            Arguments.of(0, "abcde xyz ecdab"),
            Arguments.of(1, "a ab abc abd abf abj"),
            Arguments.of(1, "iiii oiii ooii oooi oooo"),
            Arguments.of(0, "oiii ioii iioi iiio"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(466, 251))
}
