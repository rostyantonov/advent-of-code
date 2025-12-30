package aoc.common.resource

import java.io.File

/**
 * Utility for reading input files from resources.
 * Provides convenient methods for loading Advent of Code input files.
 */
object ResourceReader {
    /**
     * Read lines from a resource file.
     * @param resourcePath Path to resource file (e.g., "inputs/2015/day01.txt")
     * @return List of lines from the file
     * @throws IllegalArgumentException if resource not found
     */
    fun readLines(resourcePath: String): List<String> {
        val resource =
            this::class.java.classLoader.getResource(resourcePath)
                ?: throw IllegalArgumentException("Resource not found: $resourcePath")
        return File(resource.toURI()).readLines()
    }

    /**
     * Read entire content of a resource file as a single string.
     * @param resourcePath Path to resource file
     * @return File content as string
     * @throws IllegalArgumentException if resource not found
     */
    fun readText(resourcePath: String): String {
        val resource =
            this::class.java.classLoader.getResource(resourcePath)
                ?: throw IllegalArgumentException("Resource not found: $resourcePath")
        return File(resource.toURI()).readText()
    }

    /**
     * Read lines from an Advent of Code input file using standard naming convention.
     * @param year Year (e.g., 2015)
     * @param day Day number (1-25)
     * @return List of lines from the input file
     */
    fun readAoCInput(
        year: Int,
        day: Int,
    ): List<String> {
        val dayStr = day.toString().padStart(2, '0')
        return readLines("inputs/$year/day$dayStr.txt")
    }

    /**
     * Read lines from an Advent of Code example input file.
     * @param year Year (e.g., 2015)
     * @param day Day number (1-25)
     * @param exampleNumber Example number (default 1 for first example)
     * @return List of lines from the example file
     */
    fun readAoCExample(
        year: Int,
        day: Int,
        exampleNumber: Int = 1,
    ): List<String> {
        val dayStr = day.toString().padStart(2, '0')
        val exampleSuffix = if (exampleNumber == 1) "" else "_$exampleNumber"
        return readLines("inputs/$year/day${dayStr}_example$exampleSuffix.txt")
    }

    /**
     * Check if a resource exists.
     * @param resourcePath Path to resource file
     * @return true if resource exists, false otherwise
     */
    fun exists(resourcePath: String): Boolean = this::class.java.classLoader.getResource(resourcePath) != null

    /**
     * Check if an Advent of Code input file exists.
     * @param year Year
     * @param day Day number
     * @return true if input file exists
     */
    fun aocInputExists(
        year: Int,
        day: Int,
    ): Boolean {
        val dayStr = day.toString().padStart(2, '0')
        return exists("inputs/$year/day$dayStr.txt")
    }
}
