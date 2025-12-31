package aoc.common.entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PositionTest {
    @Test
    fun `test position creation`() {
        val pos = Position(5, 10)
        assertEquals(5, pos.row)
        assertEquals(10, pos.col)
    }

    @Test
    fun `test position movement methods`() {
        val origin = Position(0, 0)

        assertEquals(Position(-1, 0), origin.getUp())
        assertEquals(Position(1, 0), origin.getDown())
        assertEquals(Position(0, -1), origin.getLeft())
        assertEquals(Position(0, 1), origin.getRight())
    }

    @Test
    fun `test diagonal movement methods`() {
        val origin = Position(0, 0)

        assertEquals(Position(-1, -1), origin.getUpLeft())
        assertEquals(Position(-1, 1), origin.getUpRight())
        assertEquals(Position(1, -1), origin.getDownLeft())
        assertEquals(Position(1, 1), origin.getDownRight())
    }

    @Test
    fun `test position addition operator`() {
        val pos1 = Position(3, 4)
        val pos2 = Position(1, 2)

        assertEquals(Position(4, 6), pos1 + pos2)
    }

    @Test
    fun `test position subtraction operator`() {
        val pos1 = Position(5, 7)
        val pos2 = Position(2, 3)

        assertEquals(Position(3, 4), pos1 - pos2)
    }

    @Test
    fun `test position equality`() {
        val pos1 = Position(1, 2)
        val pos2 = Position(1, 2)
        val pos3 = Position(2, 1)

        assertEquals(pos1, pos2)
        assert(pos1 != pos3)
    }

    @Test
    fun `test manhattan distance`() {
        val pos1 = Position(0, 0)
        val pos2 = Position(3, 4)
        val pos3 = Position(-2, 5)

        assertEquals(7, pos1.manhattanDistance(pos2))
        assertEquals(0, pos1.manhattanDistance(pos1))
        assertEquals(7, pos1.manhattanDistance(pos3))
        assertEquals(6, pos2.manhattanDistance(pos3))
    }

    @Test
    fun `test adjacent positions`() {
        val origin = Position(5, 5)
        val adjacent = origin.adjacentPositions()

        assertEquals(4, adjacent.size)
        assert(adjacent.contains(Position(5, 4))) // up
        assert(adjacent.contains(Position(5, 6))) // down
        assert(adjacent.contains(Position(4, 5))) // left
        assert(adjacent.contains(Position(6, 5))) // right
    }

    @Test
    fun `test neighbors`() {
        val origin = Position(5, 5)
        val neighbors = origin.neighbors()

        assertEquals(8, neighbors.size)
        // Check orthogonal neighbors
        assert(neighbors.contains(Position(5, 4))) // up
        assert(neighbors.contains(Position(5, 6))) // down
        assert(neighbors.contains(Position(4, 5))) // left
        assert(neighbors.contains(Position(6, 5))) // right
        // Check diagonal neighbors
        assert(neighbors.contains(Position(4, 4))) // up-left
        assert(neighbors.contains(Position(6, 4))) // up-right
        assert(neighbors.contains(Position(4, 6))) // down-left
        assert(neighbors.contains(Position(6, 6))) // down-right
    }
}
