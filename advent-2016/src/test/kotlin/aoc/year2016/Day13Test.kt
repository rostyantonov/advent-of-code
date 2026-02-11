package aoc.year2016

import aoc.common.AoCEmptyTest
import aoc.common.IAoCSingleFunctionTest
import aoc.common.entity.Position
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day13Test :
    AoCEmptyTest<Day13, Int>(),
    IAoCSingleFunctionTest<Day13, Int, Position, Int> {
    override val partOneFunc: (Position) -> Int
        get() = currentDay::solve

    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day13()
    }

    // or example, if the office designer's favorite number were 10, drawing walls as # and open spaces as .,
    // the corner of the building containing 0,0 would look like this:
    //        0123456789
    //      0 .#.####.##
    //      1 ..#..#...#
    //      2 #....##...
    //      3 ###.#.###.
    //      4 .##..#..#.
    //      5 ..##....#.
    //      6 #...##.###
    //
    // Now, suppose you wanted to reach 7,4. The shortest route you could take is marked as O:
    //        0123456789
    //      0 .#.####.##
    //      1 .O#..#...#
    //      2 #OOO.##...
    //      3 ###O#.###.
    //      4 .##OO#OO#.
    //      5 ..##OOO.#.
    //      6 #...##.###
    //
    // Thus, reaching 7,4 would take a minimum of 11 steps (starting from your current location, 1,1).
    override fun partOneFunctionInput(): Stream<Arguments> = Stream.of(Arguments.of(11, 10, Position(7, 4)))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(90, 135))
}
