package aoc.year2016

import aoc.common.entity.AsmComputer
import aoc.common.entity.AsmComputer.A_REG
import aoc.common.entity.AsmComputer.B_REG
import aoc.common.entity.AsmComputer.C_REG
import aoc.common.entity.AsmComputer.D_REG
import aoc.common.entity.AsmInstruction
import aoc.common.entity.AsmInstruction.Out
import aoc.common.entity.AsmInstructionCompanion
import aoc.common.entity.AsmInstructionPatterns
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput

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

    private fun producesClockSignal(
        initialA: Int,
        requiredSignals: Int = 20,
        maxSteps: Int = 1_000_000,
    ): Boolean {
        val registers =
            AsmComputer.createRegisters(
                A_REG to initialA,
                B_REG to 0,
                C_REG to 0,
                D_REG to 0,
            )
        val instructions = input
        var position = 0
        var expected = 0
        var produced = 0
        var steps = 0

        while (position in instructions.indices && steps < maxSteps && produced < requiredSignals) {
            val instruction = instructions[position]
            if (instruction is Out) {
                val signal = AsmComputer.getValue(instruction.value, registers)
                if (signal != expected) return false
                expected = 1 - expected
                produced++
                position += 1
            } else {
                position += instruction.execute(registers)
            }
            steps++
        }
        return produced >= requiredSignals
    }
}
