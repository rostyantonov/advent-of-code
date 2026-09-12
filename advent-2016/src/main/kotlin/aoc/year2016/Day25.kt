package aoc.year2016

import aoc.common.entity.asm.AsmComputer.Companion.A_REG
import aoc.common.entity.asm.AsmInstruction
import aoc.common.entity.asm.AsmInstructionCompanion
import aoc.common.entity.asm.AsmInstructionPatterns
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput
import aoc.year2016.entity.ClockSignalComputer

class Day25 : AoCFileInput<List<AsmInstruction>, Int>() {
    override val inputFunction
        get() =
            StructuredMultiInput(
                regexArray =
                    arrayOf(
                        AsmInstructionPatterns.CPY_REG,
                        AsmInstructionPatterns.INC_REG,
                        AsmInstructionPatterns.DEC_REG,
                        AsmInstructionPatterns.JNZ_REG,
                        AsmInstructionPatterns.OUT_REG,
                    ),
                builder = AsmInstructionCompanion::fromLine,
            )::getStructInput

    /**
     * Day 25: Clock Signal
     *
     * Find the smallest integer to initialize register a so the output produced by `out`
     * starts with an alternating 0,1,0,1,... clock signal.
     */
    override fun processPartOne(): Int {
        var a = 0
        while (true) {
            if (producesClockSignal(a)) return a
            a++
        }
    }
    // result 182 for part 1

    /**
     * No second part.
     */
    override fun processPartTwo(): Int = -1
    // no task two

    private fun producesClockSignal(initialA: Int): Boolean =
        ClockSignalComputer(input, mapOf(A_REG to initialA.toLong()))
            .apply { run(MAX_STEPS) }
            .valid

    companion object {
        /** Guard for seed values whose program never emits, so the brute force cannot hang. */
        private const val MAX_STEPS = 1_000_000L
    }
}
