package aoc.year2016

import aoc.common.entity.asm.AsmComputer.Companion.A_REG
import aoc.common.entity.asm.AsmInstruction
import aoc.common.entity.asm.AsmInstructionCompanion
import aoc.common.entity.asm.AsmInstructionPatterns
import aoc.common.entity.asm.SimpleAsmComputer
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput

class Day23 : AoCFileInput<List<AsmInstruction>, Int>() {
    override val inputFunction
        get() =
            StructuredMultiInput(
                regexArray =
                    arrayOf(
                        AsmInstructionPatterns.CPY_REG,
                        AsmInstructionPatterns.INC_REG,
                        AsmInstructionPatterns.DEC_REG,
                        AsmInstructionPatterns.JNZ_REG,
                        AsmInstructionPatterns.TGL_REG,
                    ),
                builder = AsmInstructionCompanion::fromLine,
            )::getStructInput

    /**
     * This is one of the top floors of the nicest tower in EBHQ. The Easter Bunny's private office is here,
     * complete with a safe hidden behind a painting, and who wouldn't hide a star in a safe behind a painting?
     *
     * The safe has a digital screen and keypad for code entry. A sticky note attached to the safe has a
     * password hint on it: "eggs". The painting is of a large rabbit coloring some eggs. You see 7.
     *
     * When you go to type the code, though, nothing appears on the display; instead, the keypad comes apart
     * in your hands, apparently having been smashed. Behind it is some kind of socket - one that matches a
     * connector in your prototype computer! You pull apart the smashed keypad and extract the logic circuit,
     * plug it into your computer, and plug your computer into the safe.
     *
     * Now, you just need to figure out what output the keypad would have sent to the safe. You extract the
     * assembunny code from the logic chip (your puzzle input).
     *
     * The code looks like it uses almost the same architecture and instruction set that the monorail computer
     * used! You should be able to use the same assembunny interpreter for this as you did there, but with one
     * new instruction:
     *
     * tgl x toggles the instruction x away (pointing at instructions like jnz does: positive means forward;
     * negative means backward):
     *
     * - For one-argument instructions, inc becomes dec, and all other one-argument instructions become inc.
     * - For two-argument instructions, jnz becomes cpy, and all other two-instructions become jnz.
     * - The arguments of a toggled instruction are not affected.
     * - If an attempt is made to toggle an instruction outside the program, nothing happens.
     * - If toggling produces an invalid instruction (like cpy 1 2) and an attempt is later made to execute
     *   that instruction, skip it instead.
     * - If tgl toggles itself (for example, if a is 0, tgl a would target itself and become inc a),
     *   the resulting instruction is not executed until the next time it is reached.
     *
     * For example, given this program:
     *
     *      cpy 2 a
     *      tgl a
     *      tgl a
     *      tgl a
     *      cpy 1 a
     *      dec a
     *      dec a
     *
     * - cpy 2 a initializes register a to 2.
     * - The first tgl a toggles an instruction a (2) away from it, which changes the third tgl a into inc a.
     * - The second tgl a also modifies an instruction 2 away from it, which changes the cpy 1 a into jnz 1 a.
     * - The fourth line, which is now inc a, increments a to 3.
     * - Finally, the fifth line, which is now jnz 1 a, jumps a (3) instructions ahead, skipping the dec a
     *   instructions.
     *
     * In this example, the final value in register a is 3.
     *
     * The rest of the electronics seem to place the keypad entry (the number of eggs, 7) in register a,
     * run the code, and then send the value left in register a to the safe.
     *
     * What value should be sent to the safe?
     */
    override fun processPartOne(): Int = doComputations(initialA = 7L)

    /**
     * The safe doesn't open, but it does make several angry noises to express its frustration.
     *
     * You're quite sure your logic is working correctly, so the only other thing is... you check the painting
     * again. As it turns out, colored eggs are still eggs. Now you count 12.
     *
     * As you run the program with this new input, the prototype computer begins to overheat. You wonder what's
     * taking so long, and whether the lack of any instruction more powerful than "add one" has anything to do
     * with it. Don't bunnies usually multiply?
     *
     * Anyway, what value should actually be sent to the safe?
     */
    override fun processPartTwo(): Int = doComputations(initialA = 12L)

    private fun doComputations(initialA: Long): Int {
        // The computer is only the register file here, this day still drives its own loop
        val computer = SimpleAsmComputer(input, mapOf(A_REG to initialA))

        // Create mutable copy of instructions for toggle operations
        val instructions = input.toMutableList()

        fun isRegister(operand: String): Boolean = operand.toLongOrNull() == null

        // Toggle instruction at given position
        fun toggleInstruction(pos: Int) {
            if (pos !in instructions.indices) return

            instructions[pos] =
                when (val ins = instructions[pos]) {
                    is AsmInstruction.Inc -> AsmInstruction.Dec(ins.register)
                    is AsmInstruction.Dec -> AsmInstruction.Inc(ins.register)
                    is AsmInstruction.Tgl -> AsmInstruction.Inc(ins.register)
                    is AsmInstruction.Jnz -> AsmInstruction.Cpy(ins.valueOrRegister, ins.offsetOrRegister)
                    is AsmInstruction.Cpy -> AsmInstruction.Jnz(ins.valueOrRegister, ins.register2)
                    else -> ins // Other instructions unchanged
                }
        }

        // Detect and optimize multiplication pattern
        // Pattern: cpy 0 a; cpy b c; inc a; dec c; jnz c -2; dec d; jnz d -5
        // This computes a = b * d, then sets c = 0 and d = 0
        fun detectMultiplication(pos: Int): Boolean {
            if (pos + 6 >= instructions.size) return false

            val i0 = instructions[pos]
            val i1 = instructions[pos + 1]
            val i2 = instructions[pos + 2]
            val i3 = instructions[pos + 3]
            val i4 = instructions[pos + 4]
            val i5 = instructions[pos + 5]
            val i6 = instructions[pos + 6]

            // Match pattern: cpy 0 reg1; cpy reg2 reg3; inc reg1; dec reg3; jnz reg3 -2; dec reg4; jnz reg4 -5
            if (i0 is AsmInstruction.Cpy &&
                i0.valueOrRegister == "0" &&
                i1 is AsmInstruction.Cpy &&
                isRegister(i1.valueOrRegister) &&
                isRegister(i1.register2) &&
                i2 is AsmInstruction.Inc &&
                i3 is AsmInstruction.Dec &&
                i4 is AsmInstruction.Jnz &&
                i4.offsetOrRegister == "-2" &&
                i5 is AsmInstruction.Dec &&
                i6 is AsmInstruction.Jnz &&
                i6.offsetOrRegister == "-5"
            ) {
                val destReg = i0.register2
                val srcReg = i1.valueOrRegister
                val loopReg1 = i3.register
                val loopReg2 = i5.register

                // Verify the registers match the pattern
                if (destReg == i2.register &&
                    loopReg1 == i1.register2 &&
                    loopReg1 == i4.valueOrRegister &&
                    loopReg2 == i6.valueOrRegister
                ) {
                    // Perform multiplication: dest = src * loopReg2
                    val result = computer.value(srcReg) * computer.value(loopReg2)
                    computer[destReg] = result
                    computer[loopReg1] = 0L
                    computer[loopReg2] = 0L
                    return true
                }
            }
            return false
        }

        // Detect and optimize nested addition pattern
        // Pattern: cpy X c; jnz Y d; inc a; inc d; jnz d -2; inc c; jnz c -5
        // This adds X * Y to a
        fun detectAddition(pos: Int): Boolean {
            if (pos + 6 >= instructions.size) return false

            val i0 = instructions[pos]
            val i1 = instructions[pos + 1]
            val i2 = instructions[pos + 2]
            val i3 = instructions[pos + 3]
            val i4 = instructions[pos + 4]
            val i5 = instructions[pos + 5]
            val i6 = instructions[pos + 6]

            // Match pattern: cpy X reg1; jnz Y reg2; inc reg3; inc reg2; jnz reg2 -2; inc reg1; jnz reg1 -5
            if (i0 is AsmInstruction.Cpy &&
                i1 is AsmInstruction.Jnz &&
                i2 is AsmInstruction.Inc &&
                i3 is AsmInstruction.Inc &&
                i4 is AsmInstruction.Jnz &&
                i4.offsetOrRegister == "-2" &&
                i5 is AsmInstruction.Inc &&
                i6 is AsmInstruction.Jnz &&
                i6.offsetOrRegister == "-5"
            ) {
                val outerCountReg = i0.register2
                val innerCountReg = i1.offsetOrRegister
                val targetReg = i2.register

                // Verify registers match
                if (innerCountReg == i3.register &&
                    innerCountReg == i4.valueOrRegister &&
                    outerCountReg == i5.register &&
                    outerCountReg == i6.valueOrRegister
                ) {
                    // Get the constant values
                    val outerCount = computer.value(i0.valueOrRegister)
                    val innerCount = computer.value(i1.valueOrRegister)

                    // Add outer * inner to target
                    computer[targetReg] = computer.value(targetReg) + (outerCount * innerCount)
                    computer[outerCountReg] = 0L
                    // innerCountReg ends up at innerCount (not 0)
                    computer[innerCountReg] = innerCount
                    return true
                }
            }
            return false
        }

        var position = 0
        while (position in instructions.indices) {
            // Try to detect and optimize patterns
            if (detectMultiplication(position)) {
                position += 7 // Skip the entire multiplication block
                continue
            }

            if (detectAddition(position)) {
                position += 7 // Skip the entire addition block
                continue
            }

            val ins = instructions[position]

            // Handle Tgl specially since it modifies the instruction list
            if (ins is AsmInstruction.Tgl) {
                val offset = computer.value(ins.register).toInt()
                toggleInstruction(position + offset)
                position += 1 // Tgl always advances by 1
            } else {
                // Validate instruction before executing (toggled instructions may be invalid)
                val jump =
                    when (ins) {
                        is AsmInstruction.Cpy -> {
                            // Only valid if target is a register
                            if (isRegister(ins.register2)) ins.execute(computer) else 1
                        }

                        is AsmInstruction.Inc -> {
                            // Only valid if target is a register
                            if (isRegister(ins.register)) ins.execute(computer) else 1
                        }

                        is AsmInstruction.Dec -> {
                            // Only valid if target is a register
                            if (isRegister(ins.register)) ins.execute(computer) else 1
                        }

                        else -> {
                            ins.execute(computer)
                        }
                    }
                position += jump
            }
        }
        return computer[A_REG].toInt()
    }
}
