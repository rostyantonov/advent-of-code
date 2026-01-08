package aoc.common.entity

/**
 * Centralized regex patterns for parsing AsmInstruction entities.
 * These patterns are used by various Advent of Code days that implement
 * assembunny-style virtual machines.
 *
 * Each pattern is defined separately for maximum flexibility and reusability.
 * Days can compose their own arrays from these individual patterns as needed.
 */
object AsmInstructionPatterns {
    private const val REGISTER = "(?<register>[abcd])" // Common register pattern
    private const val OFFSET_VALUE = "(?<offset>[\\\\+|-]\\d+)"

    // Increment register pattern for 4-register system (a, b, c, d)
    val INC_REG = Regex("(?<cmd>inc) $REGISTER")

    // Half register pattern
    val HLF_REG = Regex("(?<cmd>hlf) $REGISTER")

    // Triple register pattern
    val TPL_REG = Regex("(?<cmd>tpl) $REGISTER")

    // Jump pattern
    val JMP_REG = Regex("(?<cmd>jmp) $OFFSET_VALUE")

    // Jump if even pattern
    val JIE_REG = Regex("(?<cmd>jie) $REGISTER, $OFFSET_VALUE")

    // Jump if one pattern for 2-register system
    val JIO_REG = Regex("(?<cmd>jio) $REGISTER, $OFFSET_VALUE")
}
