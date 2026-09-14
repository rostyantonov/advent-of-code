package aoc.year2016.entity

import aoc.common.entity.asm.AsmComputer
import aoc.common.entity.asm.AsmComputer.Companion.NEXT
import aoc.common.entity.asm.AsmInstruction

/**
 * Assembunny machine that supports the self modifying `tgl` opcode of 2016 day 23.
 *
 * `tgl` rewrites the instruction a given distance away: one argument opcodes flip between `inc` and
 * `dec` (anything else becomes `inc`), two argument opcodes flip between `jnz` and `cpy`. Arguments
 * are left alone, so a toggle can produce nonsense such as `cpy 1 2`; those are skipped instead of
 * executed, which is handled by [intercept].
 *
 * Open so that a day can add peephole optimisations on top by overriding [intercept].
 */
open class TogglingAsmComputer(
    program: List<AsmInstruction>,
    initialRegisters: Map<String, Long> = emptyMap(),
) : AsmComputer(program, initialRegisters) {
    override fun toggle(offset: Int) {
        val target = pc + offset
        if (target !in instructions.indices) return

        instructions[target] =
            when (val instruction = instructions[target]) {
                is AsmInstruction.Increment -> AsmInstruction.Decrement(instruction.register)
                is AsmInstruction.Decrement -> AsmInstruction.Increment(instruction.register)
                is AsmInstruction.Toggle -> AsmInstruction.Increment(instruction.register)
                is AsmInstruction.JumpIfNotZero ->
                    AsmInstruction.Copy(instruction.valueOrRegister, instruction.offsetOrRegister)
                is AsmInstruction.Copy ->
                    AsmInstruction.JumpIfNotZero(instruction.valueOrRegister, instruction.register2)
                else -> instruction
            }
    }

    /**
     * Skips instructions that a toggle turned into something that cannot be executed, which is any
     * write whose destination ended up being a literal rather than a register.
     */
    override fun intercept(instruction: AsmInstruction): Int? =
        when (instruction) {
            is AsmInstruction.Copy -> NEXT.takeUnless { isRegister(instruction.register2) }
            is AsmInstruction.Increment -> NEXT.takeUnless { isRegister(instruction.register) }
            is AsmInstruction.Decrement -> NEXT.takeUnless { isRegister(instruction.register) }
            else -> null
        }

    protected fun isRegister(operand: String): Boolean = operand.toLongOrNull() == null
}
