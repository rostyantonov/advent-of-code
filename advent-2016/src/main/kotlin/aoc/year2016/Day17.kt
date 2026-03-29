package aoc.year2016

import aoc.common.entity.CharConstants.CHAR_B_LOWERCASE
import aoc.common.entity.CharConstants.CHAR_F_LOWERCASE
import aoc.common.entity.CharConstants.DOWN_CHAR
import aoc.common.entity.CharConstants.LEFT_CHAR
import aoc.common.entity.CharConstants.RIGHT_CHAR
import aoc.common.entity.CharConstants.UP_CHAR
import aoc.common.entity.Position
import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput
import aoc.year2016.entity.RoomState
import java.security.MessageDigest

class Day17 : AoCFileInput<String, String>() {
    override val inputFunction
        get() = StringInput::firstString

    private val md5 = MessageDigest.getInstance("MD5")
    private val startPosition = Position(0, 0)
    private val targetPosition = Position(3, 3)

    /**
     * You're trying to access a secure vault protected by a 4x4 grid of small rooms connected by doors.
     * You start in the top-left room (0,0) and try to reach the vault at the bottom-right (3,3).
     *
     * Doors in your current room are either open or closed (and locked) based on the hexadecimal MD5 hash
     * of your passcode (your puzzle input) followed by a sequence of uppercase characters representing
     * the path taken so far (U for up, D for down, L for left, R for right).
     *
     * Only the first four characters of the hash are used; they represent, respectively, the doors up,
     * down, left, and right from your current position. Any b, c, d, e, or f means that the corresponding
     * door is open; any other character (any number or a) means that the corresponding door is closed and locked.
     *
     * To access the vault, all you need to do is reach the bottom-right room; reaching this room opens
     * the vault and all doors in the vault open.
     *
     * For example, suppose your passcode is hijkl. Initially, you have taken no steps, and so your path
     * is empty: you simply find the MD5 hash of hijkl. The first four characters of this hash are ced9,
     * which indicate that up is open (c), down is open (e), left is open (d), and right is closed and
     * locked (9). Then, you make your first move by going down.
     *
     * Because the next hash is based on the passcode followed by your current path, now including D,
     * the hash becomes f2bc. This means your next move could be left (f), right (2), or back to where
     * you started (b); or in other words, you can essentially reverse your D move: U (b) is open.
     *
     * Given your puzzle input, what is the shortest path to reach the vault?
     */
    override fun processPartOne(): String = findShortestPath(input)
    // result RDRLDRDURD for part 1

    /**
     * You're curious how robust this security solution really is, and so you decide to find longer and
     * longer paths which still provide access to the vault. You remember that paths always end the first
     * time they reach the vault (that is, they can never pass through the vault and then return to it).
     *
     * What is the length of the longest path that reaches the vault?
     */
    override fun processPartTwo(): String = findLongestPathLength(input)
    // result 596 for part 2

    private fun findShortestPath(passcode: String): String {
        val queue = ArrayDeque<RoomState>()
        queue.add(RoomState(startPosition, ""))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.position == targetPosition) {
                return current.path
            }

            val openDoors = getOpenDoors(passcode + current.path)

            // Up (decrease row)
            if (openDoors[UP_INDEX] && current.position.row > 0) {
                queue.add(RoomState(current.position.getUp(), current.path + UP_CHAR))
            }
            // Down (increase row)
            if (openDoors[DOWN_INDEX] && current.position.row < 3) {
                queue.add(RoomState(current.position.getDown(), current.path + DOWN_CHAR))
            }
            // Left (decrease col)
            if (openDoors[LEFT_INDEX] && current.position.col > 0) {
                queue.add(RoomState(current.position.getLeft(), current.path + LEFT_CHAR))
            }
            // Right (increase col)
            if (openDoors[RIGHT_INDEX] && current.position.col < 3) {
                queue.add(RoomState(current.position.getRight(), current.path + RIGHT_CHAR))
            }
        }

        return "" // No path found
    }

    private fun findLongestPathLength(passcode: String): String {
        var longestLength = 0

        fun explore(
            position: Position,
            path: String,
        ) {
            if (position == targetPosition) {
                longestLength = maxOf(longestLength, path.length)
                return
            }

            val openDoors = getOpenDoors(passcode + path)

            // Up
            if (openDoors[UP_INDEX] && position.row > 0) {
                explore(position.getUp(), path + UP_CHAR)
            }
            // Down
            if (openDoors[DOWN_INDEX] && position.row < 3) {
                explore(position.getDown(), path + DOWN_CHAR)
            }
            // Left
            if (openDoors[LEFT_INDEX] && position.col > 0) {
                explore(position.getLeft(), path + LEFT_CHAR)
            }
            // Right
            if (openDoors[RIGHT_INDEX] && position.col < 3) {
                explore(position.getRight(), path + RIGHT_CHAR)
            }
        }

        explore(startPosition, "")
        return longestLength.toString()
    }

    private fun getOpenDoors(key: String): BooleanArray {
        val hash =
            md5
                .digest(key.toByteArray())
                .take(2) // First 2 bytes = 4 hex characters
                .joinToString("") { "%02x".format(it) }

        return BooleanArray(4) { i ->
            hash[i] in CHAR_B_LOWERCASE..CHAR_F_LOWERCASE
        }
    }

    companion object {
        private const val UP_INDEX = 0
        private const val DOWN_INDEX = 1
        private const val LEFT_INDEX = 2
        private const val RIGHT_INDEX = 3
    }
}
