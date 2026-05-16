package aoc.year2016

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2016.entity.StorageNode
import aoc.year2016.entity.StorageNodeCompanion
import kotlin.math.abs

class Day22 : AoCFileInput<List<StorageNode>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex =
                    Regex(
                        """/dev/grid/node-x(?<x>\d+)-y(?<y>\d+)\s+(?<size>\d+)T\s+(?<used>\d+)T\s+(?<avail>\d+)T\s+\d+%""",
                    ),
                builder = StorageNodeCompanion::fromLine,
                skipHeaderLines = 2,
            )::getStructInput

    /**
     * You gain access to a massive storage cluster arranged in a grid; each storage node is only connected to the
     * four nodes directly adjacent to it (three if the node is on an edge, two if it's in a corner).
     *
     * You can directly access data only on node /dev/grid/node-x0-y0, but you can perform some limited actions on
     * the other nodes:
     *
     * - You can get the disk usage of all nodes (via df). The result of doing this is in your puzzle input.
     * - You can instruct a node to move (not copy) all of its data to an adjacent node (if the destination node
     *   has enough space to receive the data). The sending node is left empty after this operation.
     *
     * Nodes are named by their position: the node named node-x10-y10 is adjacent to nodes node-x9-y10, node-x11-y10,
     * node-x10-y9, and node-x10-y11.
     *
     * Before you begin, you need to understand the arrangement of data on these nodes. Even though you can only move
     * data between directly connected nodes, you're going to need to rearrange a lot of the data to get access to
     * the data you need. Therefore, you need to work out how you're going to rearrange the data.
     *
     * To do this, you'd like to count the number of viable pairs of nodes. A viable pair is any two nodes (A,B),
     * regardless of whether they are directly connected, such that:
     *
     * - Node A is not empty (its Used is not zero).
     * - Nodes A and B are not the same node.
     * - The data on node A (its Used) would fit on node B (its Avail).
     *
     * How many viable pairs of nodes are there?
     */
    override fun processPartOne(): Int = countViablePairs(input)
    // result 903 for part 1

    /**
     * --- Part Two ---
     *
     * Now that you have a better understanding of the grid, it's time to get to work.
     *
     * Your goal is to gain access to the data which begins in the node with y=0 and the highest x (that is, the
     * node in the top-right corner).
     *
     * For example, suppose you have the following grid:
     *
     *      Filesystem            Size  Used  Avail  Use%
     *      /dev/grid/node-x0-y0   10T    8T     2T   80%
     *      /dev/grid/node-x0-y1   11T    6T     5T   54%
     *      /dev/grid/node-x0-y2   32T   28T     4T   87%
     *      /dev/grid/node-x1-y0    9T    7T     2T   77%
     *      /dev/grid/node-x1-y1    8T    0T     8T    0%
     *      /dev/grid/node-x1-y2   11T    7T     4T   63%
     *      /dev/grid/node-x2-y0   10T    6T     4T   60%
     *      /dev/grid/node-x2-y1    9T    8T     1T   88%
     *      /dev/grid/node-x2-y2    9T    6T     3T   66%
     *
     * In this example, you have a storage grid 3 nodes wide and 3 nodes tall. The node you can access directly,
     * node-x0-y0, is almost full. The node containing the data you want to access, node-x2-y0 (because it has
     * y=0 and the highest x value), contains 6 terabytes of data - enough to fit on your node, if only you could
     * make enough space to move it there.
     *
     * Fortunately, node-x1-y1 looks like it has enough space to enable you to move some of this data around.
     * In fact, it seems like all of the nodes have enough space to hold any node's data (except node-x0-y2,
     * which is much larger, very full, and not moving any time soon). So, initially, the grid's capacities and
     * connections look like this:
     *
     *      ( 8T/10T) --  7T/ 9T -- [ 6T/10T]
     *          |           |           |
     *        6T/11T  --  0T/ 8T --   8T/ 9T
     *          |           |           |
     *       28T/32T  --  7T/11T --   6T/ 9T
     *
     * The node you can access directly is in parentheses; the data you want starts in the node marked by square
     * brackets.
     *
     * In this example, most of the nodes are interchangeable: they're full enough that no other node's data would
     * fit, but small enough that their data could be moved around. Let's draw these nodes as .. The exceptions
     * are the empty node, which we'll draw as _, and the very large, very full node, which we'll draw as #. Let's
     * also draw the goal data as G. Then, it looks like this:
     *
     *      (.) .  G
     *       .  _  .
     *       #  .  .
     *
     * The goal is to move the data in the top right, G, to the node in parentheses. To do this, we can issue
     * some commands to the grid and rearrange the data:
     *
     * - Move data from node-y0-x1 to node-y1-x1, leaving node-y0-x1 empty:
     *
     *
     *      (.) _  G
     *       .  .  .
     *       #  .  .
     *
     * - Move the goal data from node-y0-x2 to node-y0-x1:
     *
     *
     *      (.) G  _
     *       .  .  .
     *       #  .  .
     *
     * - At this point, we're quite close. However, we have no deletion command, so we have to move some more
     *   data around. So, next, we move the data from node-y1-x2 to node-y0-x2:
     *
     *
     *      (.) G  .
     *       .  .  _
     *       #  .  .
     *
     * - Move the data from node-y1-x1 to node-y1-x2:
     *
     *
     *      (.) G  .
     *       .  _  .
     *       #  .  .
     *
     * - Move the data from node-y1-x0 to node-y1-x1:
     *
     *
     *      (.) G  .
     *       _  .  .
     *       #  .  .
     *
     * - Next, we can free up space on our node by moving the data from node-y0-x0 to node-y1-x0:
     *
     *
     *      (_) G  .
     *       .  .  .
     *       #  .  .
     *
     * - Finally, we can access the goal data by moving the data from node-y0-x1 to node-y0-x0:
     *
     *
     *      (G) _  .
     *       .  .  .
     *       #  .  .
     *
     * So, after 7 steps, we've accessed the data we want. Unfortunately, each of these moves takes time, and we
     * need to be efficient:
     *
     * What is the fewest number of steps required to move your goal data to node-x0-y0?
     */
    override fun processPartTwo(): Int = findShortestPath(input)
    // result 215 for part 2

    private fun countViablePairs(nodes: List<StorageNode>): Int {
        var count = 0
        for (a in nodes) {
            if (a.used == 0) continue
            for (b in nodes) {
                if (a != b && a.used <= b.avail) {
                    count++
                }
            }
        }
        return count
    }

    private fun findShortestPath(nodes: List<StorageNode>): Int {
        // Find dimensions
        val maxX = nodes.maxOf { it.x }

        // Find empty node and goal position
        val emptyNode = nodes.first { it.isEmpty }
        val goalX = maxX
        val goalY = 0

        // Find walls (nodes whose data won't fit anywhere)
        val maxAvailable = nodes.maxOf { it.avail }
        val walls =
            nodes
                .filter { it.used > maxAvailable }
                .map { Pair(it.x, it.y) }
                .toSet()

        // For large grids, use pattern recognition
        // This is a sliding puzzle where we need to move goal data from (maxX, 0) to (0, 0)
        // The pattern for most AoC inputs:
        // 1. Move empty node to the left of the goal (avoiding walls)
        // 2. Once there, each goal move left requires 5 steps (swap + reposition empty)

        val emptyX = emptyNode.x
        val emptyY = emptyNode.y

        // Calculate steps needed
        // First: move empty from its position to (goalX-1, goalY)
        var steps = 0

        if (walls.isEmpty()) {
            // No walls: simple Manhattan distance to (goalX-1, goalY)
            steps = abs(emptyX - (goalX - 1)) + abs(emptyY - goalY)
        } else {
            // With walls: need to go around them
            // Most AoC inputs have walls in a horizontal line at some Y position
            val wallY = walls.minOfOrNull { it.second } ?: 0
            val wallMinX = walls.filter { it.second == wallY }.minOfOrNull { it.first } ?: 0

            // If empty is below walls and goal is above, go around
            if (emptyY > wallY && goalY <= wallY) {
                // Need to go around the left edge of walls at x=(wallMinX-1)
                // Extra horizontal distance compared to direct path
                val goingAroundWall =
                    if (emptyX >= wallMinX && goalX - 1 >= wallMinX) {
                        // Path through (wallMinX-1): extra = horizontal_via_wall - direct_horizontal
                        emptyX + goalX - 2 * wallMinX + 1 - abs(emptyX - (goalX - 1))
                    } else {
                        0
                    }
                steps =
                    emptyY + abs(emptyX - (goalX - 1)) + goingAroundWall
            } else {
                // Direct path or empty already above walls
                steps = abs(emptyX - (goalX - 1)) + abs(emptyY - goalY)
            }
        }

        // Second: move goal from (goalX, goalY) to (0, goalY)
        // The empty is now at (goalX-1, goalY)
        // To move goal left by 1: swap (1 step)
        // To continue moving: reposition empty (4 steps) + swap (1 step) = 5 steps per move
        // Total for goalX positions: 1 swap + (goalX-1) cycles of 5 = 1 + 5*(goalX-1)
        // BUT: After the last swap to (0,0), we don't need to reposition, so it's just 1
        // Corrected: (goalX-1) cycles of 5 steps + 1 final swap = 5*(goalX-1) + 1
        // which equals 1 + 5*(goalX-1)... but that's if we need repositioning after first swap
        // Actually for the test to work (result=7), we need 1 + 5*(2-1) = 6, so steps must be 1
        // Let's stick with the formula but reconsider if first swap needs different counting
        val goalMoves =
            if (goalX > 0) {
                (goalX - 1) * 5 + 1
            } else {
                0
            }

        return steps + goalMoves
    }
}
