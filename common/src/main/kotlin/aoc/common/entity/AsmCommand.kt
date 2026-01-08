package aoc.common.entity

enum class AsmCommand {
    CPY, // COPY
    HLF, // HALF
    TPL, // TRIPLE
    INC, // INCREMENT
    DEC, // DECREMENT
    JMP, // JUMP
    JIE, // JUMP_IF_EVEN
    JIO, // JUMP_IF_ONE
    JNZ, // JUMP_IF_NOT_ZERO
}