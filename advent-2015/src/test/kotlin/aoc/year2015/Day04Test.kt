package aoc.year2015

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day04Test : AoCSingleTest<Day04, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day04()
    }

    // If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043
    // starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
    // If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash starting
    // with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
    override fun partOneInput(): Stream<Arguments> =
        Stream.of(
            Arguments.of(609_043, "abcdef"),
            Arguments.of(1_048_970, "pqrstuv"),
        )

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(282_749, 9_962_624))
}
