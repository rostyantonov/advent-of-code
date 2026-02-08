package aoc.year2016

import aoc.common.entity.CharConstants.EMPTY_SPACE
import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

class Day05 : AoCFileInput<String, String>() {
    override var rawInput = listOf("ugkcyxxp")

    override val inputFunction
        get() = StringInput::firstString

    /**
     * You are faced with a security door designed by Easter Bunny engineers that seem to have acquired most
     * of their security knowledge by watching hacking movies.
     *
     * The eight-character password for the door is generated one character at a time by finding the MD5 hash
     * of some Door ID (your puzzle input) and an increasing integer index (starting with 0).
     *
     * A hash indicates the next character in the password if its hexadecimal representation starts with five zeroes.
     * If it does, the sixth character in the hash is the next character of the password.
     *
     * For example, if the Door ID is abc:
     *
     * * The first index which produces a hash that starts with five zeroes is 3231929, which we find by hashing
     *      abc3231929; the sixth character of the hash, and thus the first character of the password, is 1.
     * * 5017308 produces the next interesting hash, which starts with 000008f82...,
     *      so the second character of the password is 8.
     * * The third time a hash starts with five zeroes is for abc5278568, discovering the character f.
     *
     * In this example, after continuing this search a total of eight times, the password is 18f47a30.
     *
     * Given the actual Door ID, what is the password?
     */
    override fun processPartOne(): String = solveInParallel(part2 = false)
    // result d4cd2ee1 for part 1

    /**
     * As the door slides open, you are presented with a second door that uses a slightly more inspired
     * security mechanism. Clearly unimpressed by the last version (in what movie is the password decrypted
     * in order?!), the Easter Bunny engineers have worked out a better solution.
     *
     * Instead of simply filling in the password from left to right, the hash now also indicates the position
     * within the password to fill. You still look for hashes that begin with five zeroes; however, now, the sixth
     * character represents the position (0-7), and the seventh character is the character to put in that position.
     *
     * A hash result of 000001f means that f is the second character in the password. Use only the first result
     * for each position, and ignore invalid positions.
     *
     * For example, if the Door ID is abc:
     *
     *     The first interesting hash is from abc3231929, which produces 0000015...;
     *          so, 5 goes in position 1: _5______.
     *     In the previous method, 5017308 produced an interesting hash;
     *          however, it is ignored, because it specifies an invalid position (8).
     *     The second interesting hash is at index 5357525, which produces 000004e...;
     *          so, e goes in position 4: _5__e___.
     *
     * You almost choke on your popcorn as the final character falls into place, producing the password 05ace8e3.
     *
     * Given the actual Door ID and this new method, what is the password?
     * Be extra proud of your solution if it uses a cinematic "decrypting" animation.
     */
    override fun processPartTwo(): String = solveInParallel(part2 = true)
    // result f2c730e5 for part 2

    private fun solveInParallel(part2: Boolean = false): String =
        runBlocking {
            val password = CharArray(PASSWORD_LENGTH) { EMPTY_SPACE }
            val foundPositions = mutableSetOf<Int>()
            var currentIndex = 0

            while (foundPositions.size < PASSWORD_LENGTH) {
                val results =
                    (currentIndex until currentIndex + BATCH_SIZE)
                        .chunked(BATCH_SIZE / Runtime.getRuntime().availableProcessors())
                        .map { chunk ->
                            async(Dispatchers.Default) {
                                val messageDigest = MessageDigest.getInstance("MD5")
                                chunk.mapNotNull { index ->
                                    val hash = messageDigest.digest("$input$index".toByteArray()).toHexString()
                                    if (hash.startsWith(HASH_PREFIX)) index to hash else null
                                }
                            }
                        }.awaitAll()
                        .flatten()
                        .sortedBy { it.first }

                for ((_, hash) in results) {
                    if (!part2) {
                        // Part 1: Simple linear filling
                        val nextPosition = foundPositions.size
                        password[nextPosition] = hash[POSITION_INDEX]
                        foundPositions.add(nextPosition)
                    } else {
                        // Part 2: Specific position filling
                        val position = hash[POSITION_INDEX].digitToIntOrNull()
                        if (position != null && position in 0 until PASSWORD_LENGTH && position !in foundPositions) {
                            password[position] = hash[CHARACTER_INDEX]
                            foundPositions.add(position)
                        }
                    }
                    if (foundPositions.size == PASSWORD_LENGTH) break
                }
                currentIndex += BATCH_SIZE
            }
            password.joinToString("")
        }

    companion object {
        private const val PASSWORD_LENGTH = 8
        private const val BATCH_SIZE = 100_000
        private const val HASH_PREFIX = "00000"
        private const val POSITION_INDEX = 5
        private const val CHARACTER_INDEX = 6
    }
}
