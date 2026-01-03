package aoc.common.grid

import aoc.common.entity.Position
import aoc.common.util.cloneData
import aoc.common.util.get

class GridArray<Type>(
    var gridData: Array<Array<Type>>,
) : IGrid<Type> {
    override fun getValue(position: Position): Type? = getValue(position.row, position.col)

    override fun getValue(
        row: Int,
        col: Int,
    ): Type? =
        try {
            gridData[row, col]
        } catch (_: IndexOutOfBoundsException) {
            null
        }

    override fun clone(): GridArray<Type> = GridArray(gridData = gridData.cloneData())
}
