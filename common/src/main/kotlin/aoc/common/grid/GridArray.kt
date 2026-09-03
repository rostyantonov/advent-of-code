package aoc.common.grid

import aoc.common.entity.Position
import aoc.common.util.cloneData
import aoc.common.util.get
import aoc.common.util.set

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

    override fun setValue(
        position: Position,
        value: Type,
    ) = setValue(position.row, position.col, value)

    override fun setValue(
        row: Int,
        col: Int,
        value: Type,
    ) {
        try {
            gridData[row, col] = value
        } catch (_: IndexOutOfBoundsException) {
        }
    }

    override fun clone(): GridArray<Type> = GridArray(gridData = gridData.cloneData())
}
