package aoc.common.input

object StringInput {
    private val EMPTY_PATTERN = Regex("\\s+")

    fun firstString(blockInput: List<String>): String = blockInput.first()

    fun asIs(blockInput: List<String>): List<String> = blockInput

    fun getStringOfStringList(blockInput: List<String>): List<List<String>> =
        blockInput.map { line ->
            line.split(EMPTY_PATTERN)
        }
}
