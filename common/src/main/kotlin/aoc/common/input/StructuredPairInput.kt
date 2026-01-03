package aoc.common.input

import aoc.common.util.splitBy

class StructuredPairInput<FirstFuncResult, SecondFuncResult>(
    private val firstFunction: (List<String>) -> FirstFuncResult,
    private val secondFunction: (List<String>) -> SecondFuncResult,
) {
    fun getStructInput(blockInput: List<String>): Pair<FirstFuncResult, SecondFuncResult> {
        val (first, second) = blockInput.splitBy(String::isEmpty)
        return Pair(firstFunction(first), secondFunction(second))
    }
}
