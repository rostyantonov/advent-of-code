package aoc.common.entity.walker

import aoc.common.entity.Position
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalkerTest {
    @Test
    fun `test walker starts at origin by default`() {
        val walker = Walker()
        assertEquals(Position(0, 0), walker.position)
    }

    @Test
    fun `test walker can start at custom position`() {
        val walker = Walker(Position(5, 10))
        assertEquals(Position(5, 10), walker.position)
    }

    @Test
    fun `test walker movement updates position`() {
        val walker = Walker()

        walker.moveUp()
        assertEquals(Position(0, -1), walker.position)

        walker.moveRight()
        assertEquals(Position(1, -1), walker.position)

        walker.moveDown()
        assertEquals(Position(1, 0), walker.position)

        walker.moveLeft()
        assertEquals(Position(0, 0), walker.position)
    }

    @Test
    fun `test walker tracks full path`() {
        val walker = Walker()
        walker.moveUp()
        walker.moveRight()
        walker.moveDown()

        val expectedPath =
            listOf(
                Position(0, 0), // starting position
                Position(0, -1), // after moveUp
                Position(1, -1), // after moveRight
                Position(1, 0), // after moveDown
            )

        assertEquals(expectedPath, walker.fullPath)
    }

    @Test
    fun `test walker fullPath is immutable`() {
        val walker = Walker()
        walker.moveUp()

        val path = walker.fullPath
        // Verify it returns List (not MutableList) - compile-time immutability
        assertEquals(2, path.size)
        assertEquals(Position(0, 0), path[0])
        assertEquals(Position(0, -1), path[1])
    }

    @Test
    fun `test walker tracks visited positions as set`() {
        val walker = Walker()
        walker.moveRight()
        walker.moveDown()
        walker.moveLeft() // Back to x=0
        walker.moveUp() // Back to origin

        val visited = walker.visitedPoints

        // Should have 4 unique positions (origin counted once)
        assertEquals(4, visited.size)
        assertTrue(Position(0, 0) in visited)
        assertTrue(Position(1, 0) in visited)
        assertTrue(Position(1, 1) in visited)
        assertTrue(Position(0, 1) in visited)
    }

    @Test
    fun `test walker multi-step movement`() {
        val walker = Walker()
        walker.moveUp(3)

        assertEquals(Position(0, -3), walker.position)
        assertEquals(4, walker.fullPath.size) // origin + 3 steps
    }

    @Test
    fun `test walker visited positions handles duplicates`() {
        val walker = Walker()
        walker.moveRight()
        walker.moveLeft() // Back to origin

        assertEquals(3, walker.fullPath.size) // 3 positions in path
        assertEquals(2, walker.visitedPoints.size) // Only 2 unique positions
    }
}
