package aoc.year2017

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day07Test : AoCDoubleTest<Day07, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day07()
    }

    // For example, if your list is the following:
    //      pbga (66)
    //      xhth (57)
    //      ebii (61)
    //      havc (66)
    //      ktlj (57)
    //      fwft (72) -> ktlj, cntj, xhth
    //      qoyq (66)
    //      padx (45) -> pbga, havc, qoyq
    //      tknk (41) -> ugml, padx, fwft
    //      jptl (61)
    //      ugml (68) -> gyxo, ebii, jptl
    //      gyxo (61)
    //      cntj (57)
    // In this example, tknk is at the bottom of the tower (the bottom program), and is holding up ugml, padx, and fwft.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of("tknk", testRawInput))

    // In the example above, 2 4 1 2 is seen again after four cycles, and so the answer in that example would be 4.
    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of("60", testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("eugwuhl", "420"))
}
