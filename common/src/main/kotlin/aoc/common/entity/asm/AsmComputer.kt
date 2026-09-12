package aoc.common.entity.asm

import aoc.common.exception.UnsupportedTypeException

/**
 * Base of every assembunny/Duet style virtual machine in this repository.
 *
 * The class owns the parts that never change between puzzles: the register file, the program
 * counter, the fetch/execute loop and the halting rules. Everything a single puzzle invents on top
 * of the shared instruction set is added by a subclass instead of by copying the loop.
 *
 * Instructions report a program counter delta from [AsmInstruction.execute].
 *
 * @param program instructions to run, copied so that self modifying programs cannot corrupt the input
 * @param initialRegisters register values to seed before the first step, everything else starts at 0
 */
abstract class AsmComputer(
    program: List<AsmInstruction>,
    initialRegisters: Map<String, Long> = emptyMap(),
) {
    protected val instructions: MutableList<AsmInstruction> = program.toMutableList()

    val registers: MutableMap<String, Long> = initialRegisters.toMutableMap()

    /** Program counter, an index into [instructions]. */
    var pc: Int = 0
        protected set

    /** Set once the program runs off either end, or when a puzzle specific stop condition hits. */
    var halted: Boolean = false
        protected set

    /** Amount of instructions executed so far, useful for step limits and opcode counting. */
    var executed: Long = 0L
        private set

    /** True while the machine can make progress on its own. */
    val running: Boolean
        get() = !halted && pc in instructions.indices

    /**
     * Resolves an operand that is either a literal number or a register name.
     * Registers that were never written default to 0.
     */
    fun value(operand: String): Long = operand.toLongOrNull() ?: registers.getOrPut(operand) { 0L }

    operator fun get(register: String): Long = registers[register] ?: 0L

    operator fun set(
        register: String,
        newValue: Long,
    ) {
        registers[register] = newValue
    }

    protected fun halt() {
        halted = true
    }

    /**
     * Handles `snd` and `out`. Machines without an output channel reject the opcode.
     */
    open fun send(sent: Long): Unit = throw UnsupportedTypeException("$OPCODE_PREFIX 'snd'/'out'")

    /**
     * Handles `tgl`, which rewrites the instruction [offset] positions away from the current [pc].
     */
    open fun toggle(offset: Int): Unit = throw UnsupportedTypeException("$OPCODE_PREFIX 'tgl'")

    /**
     * Hook that runs before [instruction] is executed. Returning a program counter delta replaces
     * the execution entirely, which is how toggled-into-invalid instructions are skipped and how a
     * peephole optimisation folds a whole loop into one step. Returning null runs it normally.
     */
    protected open fun intercept(instruction: AsmInstruction): Int? = null

    /** Executes a single instruction, unless the machine already stopped. */
    fun step() {
        if (!running) return
        val instruction = instructions[pc]
        val delta = intercept(instruction) ?: instruction.execute(this)
        executed++
        pc += delta
        if (pc !in instructions.indices) halt()
    }

    /**
     * Steps until the machine stops making progress, or until [maxSteps] instructions ran.
     * The step limit guards puzzles that brute force seed values over programs that may never end.
     */
    fun run(maxSteps: Long = Long.MAX_VALUE) {
        var steps = 0L
        while (running && steps++ < maxSteps) {
            step()
        }
    }

    companion object {
        /** Program counter delta of an instruction that simply falls through to the next one. */
        const val NEXT = 1

        const val A_REG = "a"
        const val B_REG = "b"
        const val C_REG = "c"

        private const val OPCODE_PREFIX = "This computer does not support"
    }
}
