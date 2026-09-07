package aoc.common.entity

/**
 * Knot Hash implementation (AoC 2017 day 10).
 *
 * The hash simulates tying a knot in a circle of string with [size] marks on it. Each length
 * reverses a span of the circle starting at the current position, then the position moves
 * forward by the length plus an ever growing skip size.
 *
 * @param size amount of marks in the circle, 256 for the standard hash
 */
class KnotHash(
    size: Int = DEFAULT_SIZE,
) {
    private val content = Array(size) { it }
    private var skip = 0
    private var currentPosition = 0

    /**
     * Runs a single round: reverses a span for each of the given [lengths].
     * The current position and the skip size are preserved between calls.
     */
    fun rotate(lengths: List<Int>) {
        lengths.forEach { length ->
            val endPosition = currentPosition + length
            val subList = (currentPosition until endPosition).map { index -> content[index % content.size] }
            subList.reversed().forEachIndexed { index, value ->
                content[(currentPosition + index) % content.size] = value
            }
            currentPosition += length + skip++
        }
    }

    /**
     * Multiplies the first two marks of the circle, the answer format of day 10 part one.
     */
    fun multiplyFirstTwo(): Int = content[0] * content[1]

    /**
     * Reduces the sparse hash to a dense one by XOR'ing each block of [BLOCK_SIZE] marks
     * and rendering the result as a hexadecimal string.
     */
    fun denseHash(): String =
        content
            .toList()
            .chunked(BLOCK_SIZE)
            .joinToString("") { chunk ->
                "%02x".format(chunk.fold(0) { acc, value -> acc xor value })
            }

    companion object {
        private const val DEFAULT_SIZE = 256
        private const val BLOCK_SIZE = 16
        private const val ROUNDS = 64
        private val SEED_MAGIC_NUMBERS = listOf(17, 31, 73, 47, 23)

        /**
         * Full knot hash of an ASCII string: character codes plus the standard length suffix,
         * [ROUNDS] rounds, reduced to 32 hexadecimal digits.
         */
        fun of(input: String): String {
            val lengths = input.map { it.code } + SEED_MAGIC_NUMBERS

            return KnotHash()
                .apply { repeat(ROUNDS) { rotate(lengths) } }
                .denseHash()
        }
    }
}
