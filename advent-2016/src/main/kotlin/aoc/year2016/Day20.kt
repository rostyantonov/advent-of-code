package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.LongInput

class Day20 : AoCFileInput<List<LongRange>, Long>() {
    override val inputFunction
        get() = LongInput::getLongRanges

    /**
     * TYou'd like to set up a small hidden computer here so you can use it to get back into the network later.
     * However, the corporate firewall only allows communication with certain external IP addresses.
     *
     * You've retrieved the list of blocked IPs from the firewall, but the list seems to be messy and poorly
     * maintained, and it's not clear which IPs are allowed. Also, rather than being written in dot-decimal
     * notation, they are written as plain 32-bit integers, which can have any value from 0 through 4294967295,
     * inclusive.
     *
     * For example, suppose only the values 0 through 9 were valid, and that you retrieved the following blacklist:
     *
     *      5-8
     *      0-2
     *      4-7
     *
     * The blacklist specifies ranges of IPs (inclusive of both the start and end value) that are not allowed.
     * Then, the only IPs that this firewall allows are 3 and 9, since those are the only numbers not in any range.
     *
     * Given the list of blocked IPs you retrieved from the firewall (your puzzle input), what is the
     * lowest-valued IP that is not blocked?
     */
    override fun processPartOne(): Long = solvePart1()
    // result 17 348 574 for part 1

    /**
     * How many IPs are allowed by the blacklist?
     */
    override fun processPartTwo(): Long = solvePart2()
    // result 104 for part 2

    private fun solvePart1(): Long {
        var lowestValue = 0L
        mergedRanges().forEach {
            if (lowestValue in it) {
                lowestValue = it.last + 1
            } else if (lowestValue < it.first) {
                return lowestValue
            }
        }

        return lowestValue
    }

    private fun solvePart2(): Long {
        val maxIp = 4_294_967_295L
        val blockedCount = mergedRanges().sumOf { it.last - it.first + 1 }
        return maxIp - blockedCount + 1
    }

    /**
     * Merges overlapping and adjacent IP ranges into a list of non-overlapping ranges.
     * This optimization allows both parts to reuse the same merged range list.
     */
    private fun mergedRanges(): List<LongRange> {
        val sorted = input.sortedBy { it.first }
        if (sorted.isEmpty()) return emptyList()

        val merged = mutableListOf<LongRange>()
        var current = sorted[0]

        for (i in 1 until sorted.size) {
            val next = sorted[i]
            // Check if ranges overlap or are adjacent
            if (next.first <= current.last + 1) {
                // Merge: extend current range to include next
                current = current.first..maxOf(current.last, next.last)
            } else {
                // No overlap: save current and move to next
                merged.add(current)
                current = next
            }
        }
        merged.add(current)

        return merged
    }
}
