package aoc.common.entity

import aoc.common.util.safeValue

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

    companion object : IStructureMulti<AsmInstruction> {
        override fun create(collection: MatchGroupCollection): AsmInstruction =
            when (safeValue<AsmCommand>(BaseEntity.getAsString(collection, "cmd"))) {
                AsmCommand.HLF -> Hlf(BaseEntity.getAsString(collection, "register"))
                AsmCommand.TPL -> Tpl(BaseEntity.getAsString(collection, "register"))
                AsmCommand.INC -> Inc(BaseEntity.getAsString(collection, "register"))
                AsmCommand.DEC -> Dec(BaseEntity.getAsString(collection, "register"))
                AsmCommand.JMP -> Jmp(BaseEntity.getAsNullableInt(collection, "offset")!!)
                AsmCommand.JIE ->
                    Jie(
                        register = BaseEntity.getAsString(collection, "register"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                AsmCommand.JIO ->
                    Jio(
                        register = BaseEntity.getAsString(collection, "register"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                AsmCommand.CPY ->
                    Cpy(
                        value = BaseEntity.getAsString(collection, "value"),
                        register = BaseEntity.getAsString(collection, "register"),
                    )
                AsmCommand.JNZ ->
                    Jnz(
                        value = BaseEntity.getAsString(collection, "value"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                else -> {
                    throw IllegalArgumentException("Unknown AsmCommand in AsmInstruction creation")
                }
            }
    }
}
