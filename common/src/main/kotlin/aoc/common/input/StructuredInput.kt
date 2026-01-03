package aoc.common.input

import kotlin.reflect.KFunction2

class StructuredInput<Structure>(
    private val regex: Regex?,
    private val builder: KFunction2<String, Regex?, Structure>,
) {
    fun getStructInput(blockInput: List<String>): List<Structure> =
        blockInput.map { string ->
            builder(string, regex)
        }

    fun getSingleStructInput(blockInput: List<String>): Structure =
        blockInput
            .map { string ->
                builder(string, regex)
            }.first()
}
