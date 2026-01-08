package aoc.common.entity

/**
 * Utility object for executing assembunny computer instructions.
 * Provides common functions for register management and program execution.
 */
object AsmComputer {
    const val A_REG = "a"
    const val B_REG = "b"

    /**
     * Creates a mutable map of registers with initial values.
     *
     * @param pairs Variable number of register name to initial value pairs
     * @return Mutable map of registers
     *
     */
    fun createRegisters(vararg pairs: Pair<String, Int>): MutableMap<String, Int> = mutableMapOf(*pairs)

    /**
     * Executes a list of assembunny instructions and returns the value of the specified register.
     *
     * @param instructions List of instructions to execute
     * @param registers Mutable map of registers (modified during execution)
     * @param returnRegister Name of the register whose value should be returned
     * @return Final value of the specified register
     */
    fun execute(
        instructions: List<AsmInstruction>,
        registers: MutableMap<String, Int>,
        returnRegister: String,
    ): Int {
        var position = 0
        while (position in instructions.indices) {
            position += instructions[position].execute(registers)
        }
        return registers[returnRegister] ?: 0
    }

    /**
     * Gets the integer value from either a numeric string or a register name.
     *
     * @param value String that is either a number or a register name
     * @param registers Map of register values
     * @return Integer value (the number itself or the register's value, defaulting to 0)
     */
    fun getValue(
        value: String,
        registers: Map<String, Int>,
    ): Int = value.toIntOrNull() ?: registers[value] ?: 0

    /**
     * Checks if a string represents a valid register name.
     *
     * @param value String to check
     * @param registers Map of registers
     * @return true if the value is a register name, false otherwise
     */
    fun isRegister(
        value: String,
        registers: Map<String, Int>,
    ): Boolean = value in registers
}
