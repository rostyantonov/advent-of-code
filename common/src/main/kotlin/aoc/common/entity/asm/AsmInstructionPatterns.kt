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
    private const val REGISTER2 = "(?<register2>\\w{1})" // Common register pattern
    private const val OFFSET_VALUE = "(?<offset>[+-]?\\d+)"
    private const val VALUE_OR_REGISTER = "(?<valueOrRegister>-?\\d+|\\w{1})" // Common value or register pattern
    private const val OFFSET_OR_REGISTER = "(?<offsetOrRegister>[+-]?\\d+|\\w{1})"

    val INCREMENT = Regex("(?<cmd>inc) $REGISTER")

    val HALVE = Regex("(?<cmd>hlf) $REGISTER")

    val TRIPLE = Regex("(?<cmd>tpl) $REGISTER")

    val JUMP = Regex("(?<cmd>jmp) $OFFSET_VALUE")

    val JUMP_IF_EVEN = Regex("(?<cmd>jie) $REGISTER, $OFFSET_VALUE")

    val JUMP_IF_ONE = Regex("(?<cmd>jio) $REGISTER, $OFFSET_VALUE")

    val DECREMENT = Regex("(?<cmd>dec) $REGISTER")

    val COPY = Regex("(?<cmd>cpy) $VALUE_OR_REGISTER $REGISTER2")

    val JUMP_IF_NOT_ZERO = Regex("(?<cmd>jnz) $VALUE_OR_REGISTER $OFFSET_OR_REGISTER")

    val TOGGLE = Regex("(?<cmd>tgl) $REGISTER")

    // The operand may be a literal
    val OUTPUT = Regex("(?<cmd>out) $VALUE_OR_REGISTER")

    // The operand may be a literal (the 2017 day 18 part two example sends "snd 1")
    val SEND = Regex("(?<cmd>snd) $VALUE_OR_REGISTER")

    val ASSIGN = Regex("(?<cmd>set) $REGISTER $VALUE_OR_REGISTER")

    val ADD = Regex("(?<cmd>add) $REGISTER $VALUE_OR_REGISTER")

    val MULTIPLY = Regex("(?<cmd>mul) $REGISTER $VALUE_OR_REGISTER")

    val MODULO = Regex("(?<cmd>mod) $REGISTER $VALUE_OR_REGISTER")

    val RECEIVE = Regex("(?<cmd>rcv) $REGISTER")

    // The condition operand may be a literal (real input holds "jgz 1 3")
    val JUMP_IF_POSITIVE = Regex("(?<cmd>jgz) $VALUE_OR_REGISTER $OFFSET_OR_REGISTER")
}
