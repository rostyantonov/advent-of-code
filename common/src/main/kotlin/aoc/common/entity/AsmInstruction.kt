package aoc.common.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed class AsmInstruction {
    data class Hlf(
        val register: String,
    ) : AsmInstruction()

    data class Tpl(
        val register: String,
    ) : AsmInstruction()

    data class Inc(
        val register: String,
    ) : AsmInstruction()

    data class Dec(
        val register: String,
    ) : AsmInstruction()

    data class Jmp(
        val offset: Int,
    ) : AsmInstruction()

    data class Jie(
        val register: String,
        val offset: Int,
    ) : AsmInstruction()

    data class Jio(
        val register: String,
        val offset: Int,
    ) : AsmInstruction()

    data class Cpy(
        val value: String,
        val register: String,
    ) : AsmInstruction()

    data class Jnz(
        val value: String,
        val offset: Int,
    ) : AsmInstruction()
}
