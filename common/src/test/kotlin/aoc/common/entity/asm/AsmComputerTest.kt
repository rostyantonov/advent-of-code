package aoc.common.entity.asm

import aoc.common.entity.asm.AsmComputer.Companion.A_REG
import aoc.common.entity.asm.AsmComputer.Companion.B_REG
import aoc.common.exception.UnsupportedTypeException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AsmComputerTest {
    @Test
    fun `resolves literals and registers, defaulting unseen registers to zero`() {
        val computer = SimpleAsmComputer(emptyList(), mapOf(A_REG to 7L))

        assertEquals(7L, computer.value(A_REG))
        assertEquals(-12L, computer.value("-12"))
        assertEquals(0L, computer.value(B_REG))
    }

    @Test
    fun `jgz jumps on a literal condition`() {
        // Without literal support the "1" would resolve to an empty register and never jump.
        val computer =
            SimpleAsmComputer(
                listOf(
                    AsmInstruction.Jgz("1", "2"),
                    AsmInstruction.Inc(A_REG),
                    AsmInstruction.Inc(B_REG),
                ),
            )
        computer.run()

        assertEquals(0L, computer[A_REG], "the skipped instruction must not have run")
        assertEquals(1L, computer[B_REG])
    }

    @Test
    fun `halts when the program counter leaves the program`() {
        val computer = SimpleAsmComputer(listOf(AsmInstruction.Jmp(-4)))
        computer.run()

        assertTrue(computer.halted)
        assertEquals(1L, computer.executed)
    }

    @Test
    fun `run stops at the step limit without halting`() {
        val computer = SimpleAsmComputer(listOf(AsmInstruction.Inc(A_REG), AsmInstruction.Jmp(-1)))
        computer.run(maxSteps = 10L)

        assertFalse(computer.halted)
        assertEquals(10L, computer.executed)
        assertEquals(5L, computer[A_REG])
    }

    @Test
    fun `a blocking receive parks the program counter until the machine is woken`() {
        val computer = BlockingComputer(listOf(AsmInstruction.Rcv(A_REG), AsmInstruction.Inc(B_REG)))
        computer.run()

        assertTrue(computer.blocked)
        assertEquals(0, computer.pc, "the blocked rcv must stay under the program counter")

        computer.deliver(42L)
        computer.run()

        assertEquals(42L, computer[A_REG])
        assertEquals(1L, computer[B_REG])
        assertTrue(computer.halted)
    }

    @Test
    fun `opcodes the machine does not implement are rejected`() {
        val computer = SimpleAsmComputer(listOf(AsmInstruction.Snd(A_REG)))

        assertFailsWith<UnsupportedTypeException> { computer.run() }
    }

    /** Minimal machine with an input channel, used to exercise the blocking contract of `rcv`. */
    private class BlockingComputer(
        program: List<AsmInstruction>,
    ) : AsmComputer(program) {
        private var pending: Long? = null

        fun deliver(message: Long) {
            pending = message
            unblock()
        }

        override fun recover(register: String): Int {
            val message = pending ?: return WAIT.also { block() }
            pending = null
            this[register] = message
            return NEXT
        }
    }
}
