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

    // Decrement register pattern for 4-register system (a, b, c, d)
    val DEC_REG = Regex("(?<cmd>dec) $REGISTER")

    // Copy value or register to register
    val CPY_REG = Regex("(?<cmd>cpy) $VALUE_OR_REGISTER $REGISTER2")

    // Jump if not zero pattern
    val JNZ_REG = Regex("(?<cmd>jnz) $VALUE_OR_REGISTER $OFFSET_OR_REGISTER")

    // Toggle instruction pattern
    val TGL_REG = Regex("(?<cmd>tgl) $REGISTER")

    // Output instruction pattern, the operand may be a literal
    val OUT_REG = Regex("(?<cmd>out) $VALUE_OR_REGISTER")

    // Sound pattern, the operand may be a literal
    val SND_REG = Regex("(?<cmd>snd) $VALUE_OR_REGISTER")

    // Set register pattern
    val SET_REG = Regex("(?<cmd>set) $REGISTER $VALUE_OR_REGISTER")

    // Add register pattern
    val ADD_REG = Regex("(?<cmd>add) $REGISTER $VALUE_OR_REGISTER")

    // Multiply register pattern
    val MUL_REG = Regex("(?<cmd>mul) $REGISTER $VALUE_OR_REGISTER")

    // Modulo register pattern
    val MOD_REG = Regex("(?<cmd>mod) $REGISTER $VALUE_OR_REGISTER")

    // Recover register pattern
    val RCV_REG = Regex("(?<cmd>rcv) $REGISTER")

    // Jump if greater than zero register pattern
    val JGZ_REG = Regex("(?<cmd>jgz) $REGISTER $OFFSET_OR_REGISTER")
}
