package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.IntInput

class Day20 : AoCFileInput<Int, Int>() {
    override var rawInput = listOf("36000000")

    override val inputFunction
        get() = IntInput::getFirstInt

    companion object {
        private const val MAX_HOUSES_PER_ELF_PART2 = 50
    }

    /**
     * To keep the Elves busy, Santa has them deliver some presents by hand, door-to-door.
     * He sends them down a street with infinite houses numbered sequentially: 1, 2, 3, 4, 5, and so on.
     *
     * Each Elf is assigned a number, too, and delivers presents to houses based on that number:
     *
     *     The first Elf (number 1) delivers presents to every house: 1, 2, 3, 4, 5, ....
     *     The second Elf (number 2) delivers presents to every second house: 2, 4, 6, 8, 10, ....
     *     Elf number 3 delivers presents to every third house: 3, 6, 9, 12, 15, ....
     *
     * There are infinitely many Elves, numbered starting with 1.
     * Each Elf delivers presents equal to ten times his or her number at each house.
     *
     * So, the first nine houses on the street end up like this:
     *
     *      House 1 got 10 presents.
     *      House 2 got 30 presents.
     *      House 3 got 40 presents.
     *      House 4 got 70 presents.
     *      House 5 got 60 presents.
     *      House 6 got 120 presents.
     *      House 7 got 80 presents.
     *      House 8 got 150 presents.
     *      House 9 got 130 presents.
     *
     * The first house gets 10 presents: it is visited only by Elf 1, which delivers 1 * 10 = 10 presents.
     * The fourth house gets 70 presents, because it is visited by Elves 1, 2, and 4, for a total of 10 + 20 + 40 = 70 presents.
     *
     * What is the lowest house number of the house to get at least as many presents as the number in your puzzle input?.
     */
    override fun processPartOne(): Int = findLowestPresentsPossible(10)
    // result 831 600 for part 1

    override fun processPartTwo(): Int = findLowestPresentsPossible(11, MAX_HOUSES_PER_ELF_PART2)
    // result 884 520 for part 2

    private fun findLowestPresentsPossible(
        elfMultiple: Int,
        maxHousesPerElf: Int = Int.MAX_VALUE,
    ): Int {
        val maxHouses = input / elfMultiple

        val presents = IntArray(maxHouses)
        for (elf in 1..maxHouses) {
            var housesThisElf = 0
            var houseNo = elf
            while (houseNo <= maxHouses) {
                presents[houseNo - 1] += elf * elfMultiple

                housesThisElf++
                if (housesThisElf == maxHousesPerElf) {
                    break
                }
                houseNo += elf
            }
        }
        presents.forEachIndexed { index, housePresents ->
            if (input <= housePresents) {
                return index + 1
            }
        }
        throw Exception("No house found with at least $input presents (searched up to house $maxHouses)")
    }
}
