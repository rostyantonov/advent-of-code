package aoc.year2015

import aoc.common.entity.AsmInstruction
import aoc.common.entity.AsmInstructionCompanion
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput

class Day23 : AoCFileInput<List<AsmInstruction>, Int>() {
    override val inputFunction
        get() =
            StructuredMultiInput(
                regexArray =
                    arrayOf(
                        Regex("(?<cmd>hlf|tpl|inc) (?<register>[ab]{1})"),
                        Regex("(?<cmd>jmp) (?<offset>[\\\\+|-]\\d+)"),
                        Regex("(?<cmd>jie|jio) (?<register>[ab]{1}), (?<offset>[\\\\+|-]\\d+)"),
                    ),
                builder = AsmInstructionCompanion::fromLine,
            )::getStructInput

    /**
     * Little Jane Marie just got her very first computer for Christmas from some unknown benefactor. It comes
     * with instructions and an example program, but the computer itself seems to be malfunctioning. She's curious
     * what the program does, and would like you to help her run it.
     *
     * The manual explains that the computer supports two registers and six instructions (truly, it goes on to
     * remind the reader, a state-of-the-art technology). The registers are named a and b, can hold any non-negative
     * integer, and begin with a value of 0. The instructions are as follows:
     *
     *     hlf r sets register r to half its current value, then continues with the next instruction.
     *     tpl r sets register r to triple its current value, then continues with the next instruction.
     *     inc r increments register r, adding 1 to it, then continues with the next instruction.
     *     jmp offset is a jump; it continues with the instruction offset away relative to itself.
     *     jie r, offset is like jmp, but only jumps if register r is even ("jump if even").
     *     jio r, offset is like jmp, but only jumps if register r is 1 ("jump if one", not odd).
     *
     * All three jump instructions work with an offset relative to that instruction. The offset is always written
     * with a prefix + or - to indicate the direction of the jump (forward or backward, respectively). For example,
     * jmp +1 would simply continue with the next instruction, while jmp +0 would continuously jump back to itself
     * forever.
     *
     * The program exits when it tries to run an instruction beyond the ones defined.
     *
     * For example, this program sets a to 2, because the jio instruction causes it to skip the tpl instruction:
     *
     * inc a
     * jio a, +2
     * tpl a
     * inc a
     *
     * What is the value in register b when the program in your puzzle input is finished executing?
     */
    override fun processPartOne(): Int =
        doComputations(
            mutableMapOf(
                A_REG to INITIAL_REGISTER_VALUE_PART1,
                B_REG to INITIAL_REGISTER_VALUE_PART1,
            ),
        )
    // result 255 for part 1

    /**
     * The unknown benefactor is very thankful for releasi-- er, helping little Jane Marie with her computer.
     * Definitely not to distract you, what is the value in register b after the program is finished executing
     * if register a starts as 1 instead?
     */
    override fun processPartTwo(): Int =
        doComputations(
            mutableMapOf(
                A_REG to INITIAL_REGISTER_VALUE_PART2,
                B_REG to INITIAL_REGISTER_VALUE_PART1,
            ),
        )
    // result 334 for part 2

    private fun doComputations(registers: MutableMap<String, Int>): Int {
        var position = 0
        while (position in input.indices) {
            var jump: Int? = null
            when (val ins = input[position]) {
                is AsmInstruction.Hlf -> registers[ins.register] = (registers[ins.register] ?: 0) / 2
                is AsmInstruction.Tpl -> registers[ins.register] = (registers[ins.register] ?: 0) * 3
                is AsmInstruction.Inc -> registers[ins.register] = (registers[ins.register] ?: 0) + 1
                is AsmInstruction.Jmp -> jump = ins.offset
                is AsmInstruction.Jie -> if ((registers[ins.register] ?: 0) % 2 == 0) jump = ins.offset
                is AsmInstruction.Jio -> if ((registers[ins.register] ?: 0) == 1) jump = ins.offset
                else -> {} // ignore other commands
            }
            position += jump ?: 1
        } // while (input.size > position)
        return registers[B_REG] ?: 0
    }

    companion object {
        const val A_REG = "a"
        const val B_REG = "b"
        private const val INITIAL_REGISTER_VALUE_PART1 = 0
        private const val INITIAL_REGISTER_VALUE_PART2 = 1
    }
}
