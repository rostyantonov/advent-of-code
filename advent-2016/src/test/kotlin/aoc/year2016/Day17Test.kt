package aoc.year2016

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day17Test : AoCDoubleTest<Day17, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day17()
    }

    // For example:
    //   - If your passcode were ihgpwlah, the shortest path would be DDRRRD.
    //   - With kglvqrro, the shortest path would be DDUDRLRRUDRD.
    //   - With ulqzkmiv, the shortest would be DRURDRUDDLLDLUURRDULRLDUUDDDRR.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("DDRRRD", "ihgpwlah"),
            Arguments.of("DDUDRLRRUDRD", "kglvqrro"),
            Arguments.of("DRURDRUDDLLDLUURRDULRLDUUDDDRR", "ulqzkmiv"),
        )

    // For example:
    //   - If your passcode were ihgpwlah, the longest path would take 370 steps.
    //   - With kglvqrro, the longest path would be 492 steps long.
    //   - With ulqzkmiv, the longest path would be 830 steps long.
    override fun partTwoInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("370", "ihgpwlah"),
            Arguments.of("492", "kglvqrro"),
            Arguments.of("830", "ulqzkmiv"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("RDRLDRDURD", "596"))
}
