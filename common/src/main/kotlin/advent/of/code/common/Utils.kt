package advent.of.code.common

/**
 * Utility functions for common operations in Advent of Code puzzles.
 */
object Utils {
    /**
     * Splits a string into a list of integers.
     */
    fun String.toInts(delimiter: String = " "): List<Int> {
        return this.split(delimiter).filter { it.isNotEmpty() }.map { it.toInt() }
    }

    /**
     * Splits a string into a list of longs.
     */
    fun String.toLongs(delimiter: String = " "): List<Long> {
        return this.split(delimiter).filter { it.isNotEmpty() }.map { it.toLong() }
    }

    /**
     * Transposes a 2D grid.
     */
    fun <T> List<List<T>>.transpose(): List<List<T>> {
        if (isEmpty()) return emptyList()
        return (0 until this[0].size).map { col ->
            (0 until size).map { row ->
                this[row][col]
            }
        }
    }

    /**
     * Gets all neighbors (4-directional) of a point in a 2D grid.
     */
    fun getNeighbors4(row: Int, col: Int, maxRow: Int, maxCol: Int): List<Pair<Int, Int>> {
        return listOf(
            Pair(row - 1, col),
            Pair(row + 1, col),
            Pair(row, col - 1),
            Pair(row, col + 1)
        ).filter { (r, c) -> r in 0 until maxRow && c in 0 until maxCol }
    }

    /**
     * Gets all neighbors (8-directional) of a point in a 2D grid.
     */
    fun getNeighbors8(row: Int, col: Int, maxRow: Int, maxCol: Int): List<Pair<Int, Int>> {
        return listOf(
            Pair(row - 1, col - 1), Pair(row - 1, col), Pair(row - 1, col + 1),
            Pair(row, col - 1), Pair(row, col + 1),
            Pair(row + 1, col - 1), Pair(row + 1, col), Pair(row + 1, col + 1)
        ).filter { (r, c) -> r in 0 until maxRow && c in 0 until maxCol }
    }

    /**
     * Calculates the Manhattan distance between two points.
     */
    fun manhattanDistance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
        return kotlin.math.abs(p1.first - p2.first) + kotlin.math.abs(p1.second - p2.second)
    }
}
