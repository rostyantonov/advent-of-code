package aoc.common.input

object IntInput {
    private val DIGITS_PATTERN = Regex("\\d+")

    fun getFirstInt(blockInput: List<String>): Int = blockInput.map { it.toInt() }.first()

    fun getIntList(blockInput: List<String>): List<Int> = blockInput.map { it.toInt() }

    fun getCharsAsIntList(blockInput: List<String>): List<Int> = blockInput.first().map { it.digitToInt() }

    fun getIntOfIntList(blockInput: List<String>): List<List<Int>> =
        blockInput.map { line ->
            DIGITS_PATTERN.findAll(line).map { it.value.toInt() }.toList()
        }
}
