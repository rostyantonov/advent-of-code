package aoc.common.input

object IntInput {
    fun getIntList(blockInput: List<String>): List<Int> = blockInput.map { it.toInt() }
}
