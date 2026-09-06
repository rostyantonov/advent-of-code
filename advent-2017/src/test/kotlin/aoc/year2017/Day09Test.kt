package aoc.year2017

import aoc.common.AoCEmptyTest
import aoc.common.IAoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day09Test :
    AoCEmptyTest<Day09, Int>(),
    IAoCDoubleTest<Day09, Int> {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day09()
    }

    // Your goal is to find the total score for all groups in your input. Each group is assigned a score which
    // is one more than the score of the group that immediately contains it. (The outermost group gets a score of 1.)
    //
    //    {}, score of 1.
    //    {{{}}}, score of 1 + 2 + 3 = 6.
    //    {{},{}}, score of 1 + 2 + 2 = 5.
    //    {{{},{},{{}}}}, score of 1 + 2 + 3 + 3 + 3 + 4 = 16.
    //    {<a>,<a>,<a>,<a>}, score of 1.
    //    {{<ab>},{<ab>},{<ab>},{<ab>}}, score of 1 + 2 + 2 + 2 + 2 = 9.
    //    {{<!!>},{<!!>},{<!!>},{<!!>}}, score of 1 + 2 + 2 + 2 + 2 = 9.
    //    {{<a!>},{<a!>},{<a!>},{<ab>}}, score of 1 + 2 = 3.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1, "{}"),
            Arguments.of(6, "{{{}}}"),
            Arguments.of(5, "{{},{}}"),
            Arguments.of(16, "{{{},{},{{}}}}"),
            Arguments.of(1, "{<a>,<a>,<a>,<a>}"),
            Arguments.of(9, "{{<ab>},{<ab>},{<ab>},{<ab>}}"),
            Arguments.of(9, "{{<!!>},{<!!>},{<!!>},{<!!>}}"),
            Arguments.of(3, "{{<a!>},{<a!>},{<a!>},{<ab>}}"),
        )

    // To prove you've removed it, you need to count all of the characters within the garbage. The leading
    // and trailing < and > don't count, nor do any canceled characters or the ! doing the canceling.
    //
    //    <>, 0 characters.
    //    <random characters>, 17 characters.
    //    <<<<>, 3 characters.
    //    <{!>}>, 2 characters.
    //    <!!>, 0 characters.
    //    <!!!>>, 0 characters.
    //    <{o"i!a,<{i<a>, 10 characters.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(0, "<>"),
            Arguments.of(17, "<random characters>"),
            Arguments.of(3, "<<<<>"),
            Arguments.of(2, "<{!>}>"),
            Arguments.of(0, "<!!>"),
            Arguments.of(0, "<!!!>>"),
            Arguments.of(10, "<{o\"i!a,<{i<a>"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(11_089, 5_288))
}
