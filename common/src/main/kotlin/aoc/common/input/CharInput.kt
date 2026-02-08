package aoc.common.input

import aoc.common.grid.GridArray

object CharInput {
    fun stringAsCharList(blockInput: List<String>): List<Char> = blockInput.first().toList()

    fun getGridArray(blockInput: List<String>): GridArray<Char> =
        GridArray(
            blockInput
                .map { line ->
                    line.toList().toTypedArray()
                }.toTypedArray(),
        )

    fun gridAsCharArrays(blockInput: List<String>): Array<CharArray> =
        blockInput
            .map { line ->
                line.toList().toCharArray()
            }.toTypedArray()
}
