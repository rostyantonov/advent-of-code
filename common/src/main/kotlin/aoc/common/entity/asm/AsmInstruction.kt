package aoc.common.entity.asm

import aoc.ksp.GenerateStructure

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed interface AsmInstruction {
    fun execute(registers: MutableMap<String, Int>): Int

    fun getValueOrRegister(
        valueOrRegister: String,
        registers: MutableMap<String, Int>,
    ): Int =
        valueOrRegister.toIntOrNull()
            ?: registers.getOrPut(valueOrRegister) { 0 }

    data class Inc(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 1, Int::plus)
            return 1
        }
    }

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
            if (registers.getOrPut(register) { 0 } % 2 == 0) offset else 1
    }

    data class Jio(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int =
            if (registers.getOrPut(register) { 0 } == 1) offset else 1
    }

    data class Dec(
        val register: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers.merge(register, 1, Int::minus)
            return 1
        }
    }

    data class Cpy(
        val valueOrRegister: String,
        val register2: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int {
            registers[register2] = getValueOrRegister(valueOrRegister, registers)
            return 1
        }
    }

    data class Jnz(
        val valueOrRegister: String,
        val offsetOrRegister: String,
    ) : AsmInstruction {
        override fun execute(registers: MutableMap<String, Int>): Int =
            if (getValueOrRegister(valueOrRegister, registers) != 0) {
                getValueOrRegister(offsetOrRegister, registers)
            } else {
                1
            }
    }
}
