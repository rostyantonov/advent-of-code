package aoc.year2015

import aoc.common.AoCDoubleFunctionTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day05Test : AoCDoubleFunctionTest<Day05, Int, String, Boolean>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day05()
        partOneFunc = currentDay::checkPartOneIsNice
        partTwoFunc = currentDay::checkPartTwoIsNice
    }

    // ugknbfddgicrmopn is nice because it has at least three vowels (u...i...o...),
    //   a double letter (...dd...), and none of the disallowed substrings.
    // aaa is nice because it has at least three vowels and a double letter,
    //   even though the letters used by different rules overlap.
    // jchzalrnumimnmhp is naughty because it has no double letter.
    // haegwjzuvuyypxyu is naughty because it contains the string xy.
    // dvszwmarrgswjxmb is naughty because it contains only one vowel.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(true, emptyList<String>(), "ugknbfddgicrmopn"),
            Arguments.of(true, emptyList<String>(), "aaa"),
            Arguments.of(false, emptyList<String>(), "jchzalrnumimnmhp"),
            Arguments.of(false, emptyList<String>(), "haegwjzuvuyypxyu"),
            Arguments.of(false, emptyList<String>(), "dvszwmarrgswjxmb"),
        )

    // qjhvhtzxzqqjkmpb is nice because is has a pair that appears twice (qj)
    //   and a letter that repeats with exactly one letter between them (zxz).
    // xxyxx is nice because it has a pair that appears twice and a letter that repeats with one between,
    //   even though the letters used by each rule overlap.
    // uurcxstgmygtbstg is naughty because it has a pair (tg) but no repeat with a single letter between them.
    // ieodomkazucvgmuy is naughty because it has a repeating letter with one between (odo),
    //   but no pair that appears twice.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(true, emptyList<String>(), "qjhvhtzxzqqjkmpb"),
            Arguments.of(true, emptyList<String>(), "xxyxx"),
            Arguments.of(false, emptyList<String>(), "uurcxstgmygtbstg"),
            Arguments.of(false, emptyList<String>(), "ieodomkazucvgmuy"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(236, 51))
}
