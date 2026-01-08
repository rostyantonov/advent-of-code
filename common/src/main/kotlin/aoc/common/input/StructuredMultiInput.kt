package aoc.common.input

import kotlin.reflect.KFunction2

class StructuredMultiInput<Structure>(
    private val regexArray: Array<Regex>,
    private val builder: KFunction2<String, Array<Regex>, Structure>,
) {
    fun getStructInput(blockInput: List<String>): List<Structure> =
        blockInput.map { string ->
            builder(string, regexArray)
        }
}
