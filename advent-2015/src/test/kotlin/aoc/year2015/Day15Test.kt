package aoc.year2015

import aoc.common.AoCDoubleTest
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.test.BeforeTest

class Day15Test : AoCDoubleTest<Day15, Int>() {
    @BeforeTest
    override fun setupCurrentDay() {
        currentDay = Day15()
    }

    // For instance, suppose you have these two ingredients:
    //
    // Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
    // Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
    //
    // Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the
    // amounts of each ingredient must add up to 100) would result in a cookie with the following properties:
    //
    //     A capacity of 44*-1 + 56*2 = 68
    //     A durability of 44*-2 + 56*3 = 80
    //     A flavor of 44*6 + 56*-2 = 152
    //     A texture of 44*3 + 56*-1 = 76
    //
    // Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total
    // score of 62842880, which happens to be the best score possible given these ingredients.

    override fun partOneInput(): Stream<Arguments> = Stream.of(Arguments.of(62_842_880, testRawInput))

    override fun partTwoInput(): Stream<Arguments> = Stream.of(Arguments.of(57_600_000, testRawInput))

    override fun realResults(): Stream<Arguments> = Stream.of(Arguments.of(21_367_368, 1_766_400))
}
