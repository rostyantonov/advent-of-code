package aoc.year2016

import aoc.common.AoCSingleFunctionTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class Day18Test : AoCSingleFunctionTest<Day18, Int, Int, Int>() {
    @BeforeEach
    override fun setupCurrentDay() {
        currentDay = Day18()
        partOneFunc = currentDay::countSafeTiles
    }

    // After these steps, we now know the next row of tiles in the room:
    // .^^^^. Then, we continue on to the next row, using the same rules, and get ^^..^.
    // After determining two new rows, our map looks like this:
    //      ..^^.
    //      .^^^^
    //      ^^..^
    //
    // Here's a larger example with ten tiles per row and ten rows:
    //      .^^.^.^^^^
    //      ^^^...^..^
    //      ^.^^.^.^^.
    //      ..^^...^^^
    //      .^^^^.^^.^
    //      ^^..^.^^..
    //      ^^^^..^^^.
    //      ^..^^^^.^^
    //      .^^^..^.^^
    //      ^^.^^^..^^
    //
    // In ten rows, this larger example has 38 safe tiles.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6, "..^^.", 3),
            Arguments.of(38, ".^^.^.^^^^", 10),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1951, 20_002_936),
        )
}
