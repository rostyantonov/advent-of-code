package aoc.common.util

import aoc.common.entity.Position

operator fun <Type> Array<Array<Type>>.set(
    row: Int,
    col: Int,
    value: Type,
) {
    this[row][col] = value
}

operator fun <Type> Array<Array<Type>>.get(
    row: Int,
    col: Int,
): Type? =
    try {
        this[row][col]
    } catch (_: IndexOutOfBoundsException) {
        null
    }

operator fun <T> Array<Array<T?>>.get(position: Position): T? =
    try {
        this[position.row][position.col]
    } catch (_: IndexOutOfBoundsException) {
        null
    }

fun <Type> Array<Array<Type>>.cloneData(): Array<Array<Type>> =
    clone().also {
        it.forEachIndexed { row, rowData ->
            it[row] = rowData.clone()
        }
    }
