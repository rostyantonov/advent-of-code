package aoc.common.input

object StringInput {
    fun firstString(blockInput: List<String>): String = blockInput.first()

    fun asIs(blockInput: List<String>): List<String> = blockInput

    fun filterLines(
        blockInput: List<String>,
        skipHeaderLines: Int = 0,
        skipFooterLines: Int = 0,
    ): List<String> =
        when {
            skipHeaderLines > 0 && skipFooterLines > 0 -> {
                blockInput.drop(skipHeaderLines).dropLast(skipFooterLines)
            }

            skipHeaderLines > 0 -> {
                blockInput.drop(skipHeaderLines)
            }

            skipFooterLines > 0 -> {
                blockInput.dropLast(skipFooterLines)
            }

            else -> {
                blockInput
            }
        }
}
