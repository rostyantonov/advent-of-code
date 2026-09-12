package aoc.common.entity.asm

/**
 * Plain machine for programs that only move numbers between registers and jump around.
 */
class SimpleAsmComputer(
    program: List<AsmInstruction>,
    initialRegisters: Map<String, Long> = emptyMap(),
) : AsmComputer(program, initialRegisters) {
    companion object {
        /**
         * Runs [instructions] to completion and returns the final value of [returnRegister].
         *
         * @param registers register values to seed before the first step
         */
        fun execute(
            instructions: List<AsmInstruction>,
            vararg registers: Pair<String, Long>,
            returnRegister: String,
        ): Long = SimpleAsmComputer(instructions, mapOf(*registers)).apply { run() }[returnRegister]
    }
}
