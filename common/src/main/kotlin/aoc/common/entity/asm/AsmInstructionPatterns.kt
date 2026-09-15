package aoc.common.entity.asm

/**
 * Centralized regex patterns for parsing AsmInstruction entities.
 * These patterns are used by various Advent of Code days that implement
 * assembunny-style virtual machines.
 *
 * Each pattern is defined separately for maximum flexibility and reusability.
 * Days can compose their own arrays from these individual patterns as needed.
 */
object AsmInstructionPatterns {
    private const val REGISTER = "(?<register>\\w{1})" // Common register pattern
    private const val OFFSET_VALUE = "(?<offset>[+-]?\\d+)"

    val INCREMENT = Regex("(?<cmd>inc) $REGISTER")

    val HALVE = Regex("(?<cmd>hlf) $REGISTER")

    val TRIPLE = Regex("(?<cmd>tpl) $REGISTER")

    val JUMP = Regex("(?<cmd>jmp) $OFFSET_VALUE")

    val JUMP_IF_EVEN = Regex("(?<cmd>jie) $REGISTER, $OFFSET_VALUE")

    val JUMP_IF_ONE = Regex("(?<cmd>jio) $REGISTER, $OFFSET_VALUE")
}
