package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.IntInput
import kotlin.properties.Delegates

class Day17 : AoCFileInput<List<Int>, Int>() {
    override val inputFunction
        get() = IntInput::getIntList

    private var limit by Delegates.notNull<Int>()
    private var shortest by Delegates.notNull<Int>()
    private var count by Delegates.notNull<Int>()

    private companion object {
        private const val EGGNOG_LITERS = 150
    }

    /**
     * The elves bought too much eggnog again - 150 liters this time.
     * To fit it all into your refrigerator, you'll need to move it into smaller containers.
     * You take an inventory of the capacities of the available containers.
     *
     * For example, suppose you have containers of size 20, 15, 10, 5, and 5 liters.
     * If you need to store 25 liters, there are four ways to do it:
     *
     *     15 and 10
     *     20 and 5 (the first 5)
     *     20 and 5 (the second 5)
     *     15, 5, and 5
     *
     * Filling all containers entirely, how many different combinations of
     * containers can exactly fit all 150 liters of eggnog?
     */
    override fun processPartOne(): Int = checkPartOne(EGGNOG_LITERS)
    // result 1 304 for part 1

    /**
     * While playing with all the containers in the kitchen, another load of eggnog arrives!
     * The shipping and receiving department is requesting as many containers as you can spare.
     *
     * Find the minimum number of containers that can exactly fit all 150 liters of eggnog.
     * How many different ways can you fill that number of containers and still hold exactly 150 litres?
     *
     * In the example above, the minimum number of containers was two.
     * There were three ways to use that many containers, and so the answer there would be 3.
     */
    override fun processPartTwo(): Int = checkPartTwo(EGGNOG_LITERS)
    // result 18 for part 2

    fun checkPartOne(limit: Int): Int {
        this.limit = limit
        val sortedInput = input.sorted()
        shortest = sortedInput.size
        count = 0
        iterate(
            containers = sortedInput,
            start = 0,
            length = 1,
            size = 0,
            useShortest = false,
        )
        return count
    }

    fun checkPartTwo(limit: Int): Int {
        this.limit = limit
        val sortedInput = input.sorted()
        shortest = sortedInput.size
        count = 0
        iterate(
            containers = sortedInput,
            start = 0,
            length = 1,
            size = 0,
            useShortest = true,
        )
        return count
    }

    private fun iterate(
        containers: List<Int>,
        start: Int,
        length: Int,
        size: Int,
        useShortest: Boolean,
    ) {
        for (i in start..<containers.size) {
            val tmpSize = size + containers[i]
            when {
                useShortest && length > shortest -> {
                    break
                }

                tmpSize < limit -> {
                    if (!useShortest || length < shortest) {
                        iterate(
                            containers = containers,
                            start = i + 1,
                            length = length + 1,
                            size = size + containers[i],
                            useShortest = useShortest,
                        )
                    }
                }

                tmpSize == limit -> {
                    if (!useShortest || length == shortest) {
                        count++
                    } else if (length < shortest) {
                        shortest = length
                        count = 1
                    }
                }

                tmpSize > limit -> {
                    break
                }
            }
        }
    }
}
