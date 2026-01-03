package aoc.common.util

operator fun <Type> Array<Array<Type>>.set(
    xPos: Int,
    yPos: Int,
    value: Type,
) {
    this[yPos][xPos] = value
}

operator fun <Type> Array<Array<Type>>.get(
    xPos: Int,
    yPos: Int,
): Type? =
    try {
        this[yPos][xPos]
    } catch (_: IndexOutOfBoundsException) {
        null
    }

fun <Type> Array<Array<Type>>.cloneData(): Array<Array<Type>> =
    clone().also {
        it.forEachIndexed { y, rowData ->
            it[y] = rowData.clone()
        }
    }
