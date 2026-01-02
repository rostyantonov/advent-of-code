package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day12Test : AoCDoubleTest<Day12, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day12()
    }

    // [1,2,3] and {"a":2,"b":4} both have a sum of 6.
    // [[[3]]] and {"a":{"b":4},"c":-1} both have a sum of 3.
    // {"a":[-1,1]} and [-1,{"a":1}] both have a sum of 0.
    // [] and {} both have a sum of 0.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6, "[1,2,3]"),
            Arguments.of(6, "{\"a\":2,\"b\":4}"),
            Arguments.of(3, "[[[3]]]"),
            Arguments.of(3, "{\"a\":{\"b\":4},\"c\":-1}"),
            Arguments.of(0, "{\"a\":[-1,1]}"),
            Arguments.of(0, "[-1,{\"a\":1}]"),
            Arguments.of(0, "[]"),
            Arguments.of(0, "{}"),
        )

    // [1,2,3] still has a sum of 6.
    // [1,{"c":"red","b":2},3] now has a sum of 4, because the middle object is ignored.
    // {"d":"red","e":[1,2,3,4],"f":5} now has a sum of 0, because the entire structure is ignored.
    // [1,"red",5] has a sum of 6, because "red" in an array has no effect.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6, "[1,2,3]"),
            Arguments.of(4, "[1,{\"c\":\"red\",\"b\":2},3]"),
            Arguments.of(0, "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}"),
            Arguments.of(6, "[1,\"red\",5]"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(156_366, 96_852))
}
