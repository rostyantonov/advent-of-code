package aoc.common.entity

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
        override fun create(collection: MatchGroupCollection): AsmInstruction {
            val discriminator = BaseEntity.getAsString(collection, "cmd").uppercase()
            return when (discriminator) {
                "HLF" -> Hlf(BaseEntity.getAsString(collection, "register"))
                "TPL" -> Tpl(BaseEntity.getAsString(collection, "register"))
                "INC" -> Inc(BaseEntity.getAsString(collection, "register"))
                "DEC" -> Dec(BaseEntity.getAsString(collection, "register"))
                "JMP" -> Jmp(BaseEntity.getAsInt(collection, "offset"))
                "JIE" ->
                    Jie(
                        register = BaseEntity.getAsString(collection, "register"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                "JIO" ->
                    Jio(
                        register = BaseEntity.getAsString(collection, "register"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                "CPY" ->
                    Cpy(
                        value = BaseEntity.getAsString(collection, "value"),
                        register = BaseEntity.getAsString(collection, "register"),
                    )
                "JNZ" ->
                    Jnz(
                        value = BaseEntity.getAsString(collection, "value"),
                        offset = BaseEntity.getAsInt(collection, "offset"),
                    )
                else -> {
                    throw IllegalArgumentException("Unknown discriminator value: $discriminator in AsmInstruction creation")
                }
            }
        }
    }
}

