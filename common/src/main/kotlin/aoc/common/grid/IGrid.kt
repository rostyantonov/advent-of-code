package aoc.common.grid

import aoc.common.entity.Position

interface IGrid<Type> {
    fun getValue(position: Position): Type?

    fun getValue(
        row: Int,
        col: Int,
    ): Type?

    fun setValue(
        position: Position,
        value: Type,
    )

    fun setValue(
        row: Int,
        col: Int,
        value: Type,
    )

    fun getNeighbours(
        row: Int,
        col: Int,
    ) = getNeighbours(Position(row, col))

    fun getNeighbours(position: Position): List<Type> =
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

    fun clone(): IGrid<Type>
}
