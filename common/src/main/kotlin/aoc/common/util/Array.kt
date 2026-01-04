package aoc.common.util

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

fun <Type> Array<Array<Type>>.cloneData(): Array<Array<Type>> =
    clone().also {
        it.forEachIndexed { row, rowData ->
            it[row] = rowData.clone()
        }
    }
