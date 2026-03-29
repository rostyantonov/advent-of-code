package aoc.year2016

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream

class Day18Test :
    AoCEmptyTest<Day18, Int>(),
    IAoCSingleFunctionTest<Day18, Int, Int, Int> {
    override val partOneFunc: (Int) -> Int
        get() = currentDay::countSafeTiles

    @BeforeEach
    override fun setupCurrentDay() {
        currentDay = Day18()
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
    override fun partOneFunctionInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(6, "..^^.", 3),
            Arguments.of(38, ".^^.^.^^^^", 10),
        )

    override fun realResults(): Stream<Arguments> =
        Stream.of(
            Arguments.of(1951, 20_002_936),
        )
}
