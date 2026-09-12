package aoc.year2016

import aoc.common.entity.asm.AsmComputer.Companion.A_REG
import aoc.common.entity.asm.AsmInstruction
import aoc.common.entity.asm.AsmInstructionCompanion
import aoc.common.entity.asm.AsmInstructionPatterns
import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredMultiInput
import aoc.year2016.entity.TogglingAsmComputer

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

    private fun doComputations(initialA: Long): Int =
        PeepholeComputer(input, mapOf(A_REG to initialA))
            .apply { run() }[A_REG]
            .toInt()

    /**
     * Toggling machine that additionally recognises the two hand rolled loops the puzzle input uses
     * to multiply and to add, and folds each of them into a single step. Without that the part two
     * program takes minutes; the loops are only ever entered with the exact shape matched below.
     */
    private class PeepholeComputer(
        program: List<AsmInstruction>,
        initialRegisters: Map<String, Long>,
    ) : TogglingAsmComputer(program, initialRegisters) {
        override fun intercept(instruction: AsmInstruction): Int? =
            when {
                foldMultiplication() -> BLOCK_LENGTH
                foldAddition() -> BLOCK_LENGTH
                else -> super.intercept(instruction)
            }

        /**
         * Matches `cpy 0 a; cpy b c; inc a; dec c; jnz c -2; dec d; jnz d -5`, which computes
         * `a = b * d` and leaves both counters at zero.
         */
        private fun foldMultiplication(): Boolean {
            val block = blockAt(pc) ?: return false
            val zeroing = block[0]
            val copy = block[1]
            val increment = block[2]
            val innerDec = block[3]
            val innerJump = block[4]
            val outerDec = block[5]
            val outerJump = block[6]

            val opcodesMatch =
                zeroing is AsmInstruction.Cpy &&
                    copy is AsmInstruction.Cpy &&
                    increment is AsmInstruction.Inc &&
                    innerDec is AsmInstruction.Dec &&
                    innerJump is AsmInstruction.Jnz &&
                    outerDec is AsmInstruction.Dec &&
                    outerJump is AsmInstruction.Jnz
            if (!opcodesMatch) return false

            val destination = zeroing.register2
            val innerCounter = innerDec.register
            val outerCounter = outerDec.register
            val operandsMatch =
                zeroing.valueOrRegister == ZERO &&
                    isRegister(copy.valueOrRegister) &&
                    isRegister(copy.register2) &&
                    innerJump.offsetOrRegister == INNER_LOOP_OFFSET &&
                    outerJump.offsetOrRegister == OUTER_LOOP_OFFSET &&
                    destination == increment.register &&
                    innerCounter == copy.register2 &&
                    innerCounter == innerJump.valueOrRegister &&
                    outerCounter == outerJump.valueOrRegister
            if (!operandsMatch) return false

            this[destination] = value(copy.valueOrRegister) * value(outerCounter)
            this[innerCounter] = 0L
            this[outerCounter] = 0L
            return true
        }

        /**
         * Matches `cpy X c; jnz Y d; inc a; inc d; jnz d -2; inc c; jnz c -5`, which adds `X * Y` to
         * the target register. The inner counter ends at its own bound rather than at zero.
         */
        private fun foldAddition(): Boolean {
            val block = blockAt(pc) ?: return false
            val outerSeed = block[0]
            val innerSeed = block[1]
            val increment = block[2]
            val innerInc = block[3]
            val innerJump = block[4]
            val outerInc = block[5]
            val outerJump = block[6]

            val opcodesMatch =
                outerSeed is AsmInstruction.Cpy &&
                    innerSeed is AsmInstruction.Jnz &&
                    increment is AsmInstruction.Inc &&
                    innerInc is AsmInstruction.Inc &&
                    innerJump is AsmInstruction.Jnz &&
                    outerInc is AsmInstruction.Inc &&
                    outerJump is AsmInstruction.Jnz
            if (!opcodesMatch) return false

            val outerCounter = outerSeed.register2
            val innerCounter = innerSeed.offsetOrRegister
            val operandsMatch =
                innerJump.offsetOrRegister == INNER_LOOP_OFFSET &&
                    outerJump.offsetOrRegister == OUTER_LOOP_OFFSET &&
                    innerCounter == innerInc.register &&
                    innerCounter == innerJump.valueOrRegister &&
                    outerCounter == outerInc.register &&
                    outerCounter == outerJump.valueOrRegister
            if (!operandsMatch) return false

            val outerCount = value(outerSeed.valueOrRegister)
            val innerCount = value(innerSeed.valueOrRegister)
            val target = increment.register
            this[target] = this[target] + outerCount * innerCount
            this[outerCounter] = 0L
            this[innerCounter] = innerCount
            return true
        }

        /** The [BLOCK_LENGTH] instructions starting at [start], or null when they run past the end. */
        private fun blockAt(start: Int): List<AsmInstruction>? =
            if (start + BLOCK_LENGTH > instructions.size) null else instructions.subList(start, start + BLOCK_LENGTH)

        companion object {
            private const val BLOCK_LENGTH = 7
            private const val ZERO = "0"
            private const val INNER_LOOP_OFFSET = "-2"
            private const val OUTER_LOOP_OFFSET = "-5"
        }
    }
}
