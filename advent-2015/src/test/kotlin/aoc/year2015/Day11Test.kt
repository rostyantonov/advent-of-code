package aoc.year2015

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day11Test : AoCSingleTest<Day11, String>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day11()
    }

    // hijklmmn meets the first requirement (because it contains the straight hij)
    //   but fails the second requirement requirement (because it contains i and l).
    // abbceffg meets the third requirement (because it repeats bb and ff) but fails the first requirement.
    // abbcegjk fails the third requirement, because it only has one double letter (bb).
    // The next password after abcdefgh is abcdffaa.
    // The next password after ghijklmn is ghjaabcc, because you eventually skip all
    //   the passwords that start with ghi..., since i is not allowed.
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of("abcdffaa", "abcdefgh"),
            Arguments.of("ghjaabcc", "ghijklmn"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of("cqjxxyzz", "cqkaabcc"))
}
