package aoc.common.entity

import aoc.ksp.GenerateStructure
import kotlin.collections.set

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed interface AsmInstruction {
    fun execute(registers: MutableMap<String, Int>): Int

    data class Hlf(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 2, Int::div)
            return 1
        }
    }

    data class Tpl(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 3, Int::times)
            return 1
        }
    }

    data class Inc(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 1, Int::plus)
            return 1
        }
    }

    data class Dec(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 1, Int::minus)
            return 1
        }
    }

    data class Jmp(
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int = offset
    }

    data class Jie(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int =
            if ((registers[register] ?: 0) % 2 == 0) offset else 1
    }

    data class Jio(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int =
            if ((registers[register] ?: 0) == 1) offset else 1
    }

    data class Cpy(
        val value: String,
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            val valueToCopy = value.toIntOrNull() ?: registers[value] ?: 0
            registers[register] = valueToCopy
            return 1
        }
    }

    data class Jnz(
        val value: String,
        val offset: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            val valueToCheck = value.toIntOrNull() ?: registers[value] ?: 0
            val offsetValue = offset.toIntOrNull() ?: registers[offset] ?: 0
            return if (valueToCheck != 0) offsetValue else 1
        }
    }

    data class Tgl(
        val offset: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int = 1
    }
}
