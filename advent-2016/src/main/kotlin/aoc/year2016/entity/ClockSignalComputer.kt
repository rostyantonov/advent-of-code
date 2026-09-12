package aoc.year2016.entity

import aoc.common.entity.asm.AsmComputer
import aoc.common.entity.asm.AsmInstruction

/**
 * Assembunny machine for 2016 day 25, where `out` feeds a clock signal instead of a register.
 *
 * The machine validates the emitted values against the alternating 0,1,0,1,... pattern as they
 * arrive and halts as soon as the answer is known: on the first mismatch, or once
 * [requiredSignals] correct values have been produced.
 *
 * @param requiredSignals how many alternating values are accepted as proof of an endless clock
 */
class ClockSignalComputer(
    program: List<AsmInstruction>,
    initialRegisters: Map<String, Long> = emptyMap(),
    private val requiredSignals: Int = DEFAULT_REQUIRED_SIGNALS,
) : AsmComputer(program, initialRegisters) {
    private var expected = 0L
    private var produced = 0

    /** True once enough alternating values were emitted without a single mismatch. */
    val valid: Boolean
        get() = produced >= requiredSignals

    override fun send(sent: Long) {
        if (sent != expected) {
            halt()
            return
        }
        expected = 1L - expected
        produced++
        if (valid) halt()
    }

    companion object {
        private const val DEFAULT_REQUIRED_SIGNALS = 20
    }
}
