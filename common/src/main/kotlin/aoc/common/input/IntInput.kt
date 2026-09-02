package aoc.common.input

object IntInput {
    fun getFirstInt(blockInput: List<String>): Int = blockInput.map { it.toInt() }.first()

    fun getIntList(blockInput: List<String>): List<Int> = blockInput.map { it.toInt() }

    fun getCharsAsIntList(blockInput: List<String>): List<Int> = blockInput.first().map { it.digitToInt() }
}
