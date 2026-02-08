package aoc.year2016.entity

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

    fun decryptedName(): String = name.map { if (it == '-') ' ' else decryptChar(it) }.joinToString("")

    private fun decryptChar(char: Char): Char = 'a'.plus((char.minus('a') + id) % 26)
}
