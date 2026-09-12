package aoc.year2016

import aoc.common.entity.Position
import aoc.common.grid.GridArray
import aoc.common.input.AoCFileInput
import aoc.common.input.CharInput
import aoc.common.entity.CharConstants.CHAR_HASHTAG as WALL

class Day24 : AoCFileInput<GridArray<Char>, Int>() {
    override val inputFunction
        get() = CharInput::getGridArray

    /**
     * --- Day 24: Air Duct Spelunking ---
     *
     * You've finally met your match; the doors that provide access to the roof are locked tight,
     * and all of the controls and related electronics are inaccessible. You simply can't reach them.
     *
     * The robot that cleans the air ducts, however, can.
     *
     * It's not a very fast little robot, but you reconfigure it to be able to interface with some
     * of the exposed wires that have been routed through the HVAC system. If you can direct it to
     * each of those locations, you should be able to bypass the security controls.
     *
     * You extract the duct layout for this area from some blueprints you acquired and create a map
     * with the relevant locations marked (your puzzle input). 0 is your current location, from which
     * the cleaning robot embarks; the other numbers are (in no particular order) the locations the
     * robot needs to visit at least once each. Walls are marked as #, and open passages are marked as ..
     * Numbers aside, every part of the map will be either a wall or an open space.
     *
     * For example, suppose you have a map like the following:
     *
     * ###########
     * #0.1.....2#
     * #.#######.#
     * #4.......3#
     * ###########
     *
     * To reach all of the points of interest as quickly as possible, you would have the robot take the
     * following path:
     *
     *     0 to 4 (2 steps)
     *     4 to 1 (4 steps; it can't move diagonally)
     *     1 to 2 (6 steps)
     *     2 to 3 (2 steps)
     *
     * Since the robot isn't very fast, you need to find it the shortest route. This path is the fewest
     * steps (in the above example, a total of 14) required to start at 0 and then visit every other location
     * at least once.
     *
     * Given your actual map, and starting from location 0, what is the fewest number of steps required to
     * visit every non-0 number marked on the map at least once?
     */
    override fun processPartOne(): Int {
        val locations = findLocations()
        val distances = calculateDistances(locations)
        return findShortestPath(locations, distances, returnToStart = false)
    }

    /**
     * --- Part Two ---
     *
     * Of course, if you leave the cleaning robot somewhere weird, someone is bound to notice.
     *
     * What is the fewest number of steps required to start at 0, visit every non-0 number marked on the
     * map at least once, and then return to 0?
     */
    override fun processPartTwo(): Int {
        val locations = findLocations()
        val distances = calculateDistances(locations)
        return findShortestPath(locations, distances, returnToStart = true)
    }

    /**
     * Finds all numbered locations in the grid.
     * Note: GridArray.findValuePosition returns Position(col, row) but Position expects (row, col),
     * so we manually search to get correct coordinates.
     */
    private fun findLocations(): Map<Int, Position> {
        val locations = mutableMapOf<Int, Position>()
        input.gridData.forEachIndexed { row, rowData ->
            rowData.forEachIndexed { col, char ->
                if (char.isDigit()) {
                    locations[char.digitToInt()] = Position(row, col)
                }
            }
        }
        return locations
    }

    /**
     * Calculates shortest distances between all pairs of locations using BFS.
     */
    private fun calculateDistances(locations: Map<Int, Position>): Map<Pair<Int, Int>, Int> {
        val distances = mutableMapOf<Pair<Int, Int>, Int>()

        for ((from, fromPos) in locations) {
            val dist = bfs(fromPos)
            for ((to, toPos) in locations) {
                if (from != to) {
                    distances[from to to] = dist[toPos] ?: Int.MAX_VALUE
                }
            }
        }

        return distances
    }

    /**
     * Performs BFS from a starting position to find shortest distances to all reachable positions.
     */
    private fun bfs(start: Position): Map<Position, Int> {
        val distances = mutableMapOf<Position, Int>()
        val queue = ArrayDeque<Pair<Position, Int>>()
        queue.add(start to 0)
        distances[start] = 0

        while (queue.isNotEmpty()) {
            val (pos, dist) = queue.removeFirst()

            for (nextPos in pos.adjacentPositions()) {
                val cell = input.getValue(nextPos)
                if (cell != null && cell != WALL && nextPos !in distances) {
                    distances[nextPos] = dist + 1
                    queue.add(nextPos to dist + 1)
                }
            }
        }

        return distances
    }

    /**
     * Finds the shortest path visiting all locations using TSP solution.
     * Uses permutations since the number of locations is small (typically < 10).
     */
    private fun findShortestPath(
        locations: Map<Int, Position>,
        distances: Map<Pair<Int, Int>, Int>,
        returnToStart: Boolean,
    ): Int {
        val start = 0
        val toVisit = locations.keys.filter { it != start }

        var minDistance = Int.MAX_VALUE

        // Generate all permutations of locations to visit
        permutations(toVisit).forEach { perm ->
            var distance = 0
            var current = start

            // Visit each location in the permutation
            for (next in perm) {
                distance += distances[current to next] ?: Int.MAX_VALUE
                current = next
            }

            // Return to start if required
            if (returnToStart) {
                distance += distances[current to start] ?: Int.MAX_VALUE
            }

            minDistance = minOf(minDistance, distance)
        }

        return minDistance
    }

    /**
     * Generates all permutations of a list.
     */
    private fun <T> permutations(list: List<T>): List<List<T>> {
        if (list.isEmpty()) return listOf(emptyList())
        if (list.size == 1) return listOf(list)

        val result = mutableListOf<List<T>>()
        for (i in list.indices) {
            val element = list[i]
            val remaining = list.take(i) + list.drop(i + 1)
            for (perm in permutations(remaining)) {
                result.add(listOf(element) + perm)
            }
        }
        return result
    }
}
