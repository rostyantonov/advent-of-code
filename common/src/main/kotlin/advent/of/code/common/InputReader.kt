package advent.of.code.common

import java.io.File

/**
 * Utility object for reading puzzle input files.
 */
object InputReader {
    /**
     * Reads the input file for a specific year and day.
     * @param year The year of the puzzle
     * @param day The day of the puzzle
     * @return The content of the input file
     */
    fun readInput(year: Int, day: Int): String {
        val paddedDay = day.toString().padStart(2, '0')
        val resourcePath = "/inputs/$year/day$paddedDay.txt"
        return this::class.java.getResourceAsStream(resourcePath)?.bufferedReader()?.readText()
            ?: throw IllegalArgumentException("Input file not found: $resourcePath")
    }

    /**
     * Reads all lines from the input file.
     */
    fun readLines(year: Int, day: Int): List<String> {
        return readInput(year, day).lines().filter { it.isNotEmpty() }
    }

    /**
     * Reads the input and splits it by double newlines (useful for grouped data).
     */
    fun readGroups(year: Int, day: Int): List<String> {
        return readInput(year, day).split("\n\n")
    }
}
