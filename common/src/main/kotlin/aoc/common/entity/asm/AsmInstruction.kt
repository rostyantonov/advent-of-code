package aoc.common.entity.asm

import aoc.ksp.GenerateStructure
import aoc.ksp.StructureName

/**
 * Union of every opcode used by the assembunny (2015/2016) and Duet (2017) machines.
 *
 * An instruction only describes what it does to the [AsmComputer] it runs on and returns the
 * program counter delta. Opcodes with side effects outside the register file - `snd`, `rcv`, `out`,
 * `tgl` - delegate to the matching hook so that each puzzle can give them its own meaning without
 * touching the instruction set.
 */
@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed interface AsmInstruction {
    /**
     * Runs this instruction against [computer] and returns how far the program counter should move.
     * A delta of 0 means the instruction could not run and should be retried.
     */
    fun execute(computer: AsmComputer): Int

    @StructureName("inc")
    data class Increment(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] + 1L
            return AsmComputer.NEXT
        }
    }

    @StructureName("hlf")
    data class Halve(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] / 2L
            return AsmComputer.NEXT
        }
    }

    @StructureName("tpl")
    data class Triple(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] * 3L
            return AsmComputer.NEXT
        }
    }

    @StructureName("jmp")
    data class Jump(
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = offset
    }

    @StructureName("jie")
    data class JumpIfEven(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] % 2L == 0L) {
                offset
            } else {
                AsmComputer.NEXT
            }
    }

    @StructureName("jio")
    data class JumpIfOne(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] == 1L) {
                offset
            } else {
                AsmComputer.NEXT
            }
    }

    @StructureName("dec")
    data class Decrement(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] - 1L
            return AsmComputer.NEXT
        }
    }

    @StructureName("cpy")
    data class Copy(
        val valueOrRegister: String,
        val register2: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register2] = computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    @StructureName("jnz")
    data class JumpIfNotZero(
        val valueOrRegister: String,
        val offsetOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer.value(valueOrRegister) != 0L) {
                computer.value(offsetOrRegister).toInt()
            } else {
                AsmComputer.NEXT
            }
    }

    /** Rewrites another instruction; the rewrite rules belong to the machine. */
    @StructureName("tgl")
    data class Toggle(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer.toggle(computer.value(register).toInt())
            return AsmComputer.NEXT
        }
    }

    /** Emits a value on the output channel of the machine. */
    @StructureName("out")
    data class Output(
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer.send(computer.value(valueOrRegister))
            return AsmComputer.NEXT
        }
    }

    /** Plays a sound, or sends a message to the partner program, depending on the machine. */
    @StructureName("snd")
    data class Send(
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer.send(computer.value(valueOrRegister))
            return AsmComputer.NEXT
        }
    }

    @StructureName("set")
    data class Assign(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    @StructureName("add")
    data class Add(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] + computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    @StructureName("mul")
    data class Multiply(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] * computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    @StructureName("mod")
    data class Modulo(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] % computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    /** Recovers the last sound, or waits for a message; the machine decides which and how far to move. */
    @StructureName("rcv")
    data class Receive(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = computer.recover(register)
    }

    @StructureName("jgz")
    data class JumpIfPositive(
        val valueOrRegister: String,
        val offsetOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer.value(valueOrRegister) > 0L) {
                computer.value(offsetOrRegister).toInt()
            } else {
                AsmComputer.NEXT
            }
    }
}
