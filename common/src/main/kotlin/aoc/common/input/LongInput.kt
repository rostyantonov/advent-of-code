package aoc.common.input

import aoc.common.util.asLongRange

object LongInput {
    fun getLongRanges(blockInput: List<String>): List<LongRange> =
        blockInput.map {
            it.asLongRange()
        }
}
