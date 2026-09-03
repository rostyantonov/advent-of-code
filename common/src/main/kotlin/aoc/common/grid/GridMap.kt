package aoc.common.grid

import aoc.common.entity.Position
import aoc.common.util.cloneData

class GridMap<Type>(
    var gridData: MutableMap<Position, Type> = mutableMapOf(),
) : IGrid<Type> {
    override fun getValue(position: Position): Type? =
        try {
            gridData[position]
        } catch (_: IndexOutOfBoundsException) {
            null
        }

    override fun getValue(
        row: Int,
        col: Int,
    ): Type? = getValue(Position(row, col))

    override fun setValue(
        position: Position,
        value: Type,
    ) {
        gridData[position] = value
    }

    override fun setValue(
        row: Int,
        col: Int,
        value: Type,
    ) = setValue(Position(row, col), value)

    override fun clone(): GridMap<Type> = GridMap(gridData = gridData.cloneData())
}
