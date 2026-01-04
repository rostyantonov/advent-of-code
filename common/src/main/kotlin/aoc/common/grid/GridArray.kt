package aoc.common.grid

import aoc.common.entity.Position
import aoc.common.util.cloneData
import aoc.common.util.get

class GridArray<Type>(
    var gridData: Array<Array<Type>>,
) {
    fun getValue(position: Position): Type? =
        try {
            gridData[position.row, position.col]
        } catch (_: IndexOutOfBoundsException) {
            null
        }

    fun getValue(
        row: Int,
        col: Int,
    ): Type? =
        try {
            gridData[row, col]
        } catch (_: IndexOutOfBoundsException) {
            null
        }

    fun getNeighbours(
        row: Int,
        col: Int,
    ) = getNeighbours(Position(row, col))

    private fun getNeighbours(position: Position): List<Type> =
        listOfNotNull(
            getValue(position.getUp()),
            getValue(position.getUpRight()),
            getValue(position.getRight()),
            getValue(position.getDownRight()),
            getValue(position.getDown()),
            getValue(position.getDownLeft()),
            getValue(position.getLeft()),
            getValue(position.getUpLeft()),
        )

    fun clone(): GridArray<Type> = GridArray(gridData = gridData.cloneData())
}
