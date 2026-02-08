package aoc.year2016.entity

import aoc.common.entity.CharConstants.CHAR_A_LOWERCASE
import aoc.common.entity.CharConstants.CHAR_HYPHEN
import aoc.common.entity.CharConstants.EMPTY_SPACE
import aoc.ksp.GenerateStructure

@GenerateStructure
data class Room(
    val name: String,
    val id: Int,
    val hash: String,
) {
    fun isReal(): Boolean =
        name
            .replace("-", "")
            .toList() // list of chars should ignore dash
            .groupBy { it }
            .mapValues { it.value.size } // counting chars
            .toList() // converting to Pair<Char, count:Int>
            .sortedWith(
                compareBy<Pair<Char, Int>> { 0 - it.second }.thenBy { it.first },
            ) // sorting by char and then by size
            .take(5)
            .map { it.first }
            .joinToString("") == hash

    fun decryptedName(): String = name.map { if (it == CHAR_HYPHEN) EMPTY_SPACE else decryptChar(it) }.joinToString("")

    private fun decryptChar(char: Char): Char = CHAR_A_LOWERCASE.plus((char.minus(CHAR_A_LOWERCASE) + id) % 26)
}
