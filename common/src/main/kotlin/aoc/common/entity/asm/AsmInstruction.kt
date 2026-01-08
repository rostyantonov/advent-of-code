package aoc.common.entity.asm

import aoc.ksp.GenerateStructure

/**
 * An instruction only describes what it does to the [AsmComputer] it runs on and returns the
 * program counter delta.
 */
@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed interface AsmInstruction {
    /** Runs this instruction against [computer] and returns how far the program counter should move. */
    fun execute(computer: AsmComputer): Int

    data class Inc(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] + 1L
            return AsmComputer.NEXT
        }
    }

    data class Hlf(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] / 2L
            return AsmComputer.NEXT
        }
    }

    data class Tpl(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] * 3L
            return AsmComputer.NEXT
        }
    }

    data class Jmp(
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = offset
    }

    data class Jie(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] % 2L == 0L) {
                offset
            } else {
                AsmComputer.NEXT
            }
    }

    data class Jio(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] == 1L) {
                offset
            } else {
                AsmComputer.NEXT
            }
    }
}
