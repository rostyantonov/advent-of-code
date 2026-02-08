package aoc.year2016

import aoc.common.AoCSingleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day04Test : AoCSingleTest<Day04, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day04()
    }

    //    aaaaa-bbb-z-y-x-123[abxyz] is a real room because the most common letters are a (5), b (3),
    //          and then a tie between x, y, and z, which are listed alphabetically.
    //    a-b-c-d-e-f-g-h-987[abcde] is a real room because although the letters are all tied (1 of each),
    //          the first five are listed alphabetically.
    //    not-a-real-room-404[oarel] is a real room.
    //    totally-real-room-200[decoy] is not.
    //
    // Of the real rooms from the list above, the sum of their sector IDs is 1514.
    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(1514, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(409_147, 991))
}
