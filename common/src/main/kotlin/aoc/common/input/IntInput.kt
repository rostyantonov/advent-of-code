package aoc.common.input

object IntInput {
    private val EMPTY_PATTERN = Regex("\\s+")

    fun getFirstInt(blockInput: List<String>): Int = blockInput.map { it.toInt() }.first()

    fun getFirstLineIntList(blockInput: List<String>): List<Int> =
        blockInput.first().split(EMPTY_PATTERN).map {
            it.toInt()
        }

    fun getIntList(blockInput: List<String>): List<Int> = blockInput.map { it.toInt() }

    fun getCharsAsIntList(blockInput: List<String>): List<Int> = blockInput.first().map { it.digitToInt() }

    fun getIntOfIntList(blockInput: List<String>): List<List<Int>> =
        blockInput.map { line ->
            line.split(EMPTY_PATTERN).map { it.toInt() }
        }
}
