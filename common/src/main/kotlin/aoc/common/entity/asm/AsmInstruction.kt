package aoc.common.entity.asm

import aoc.ksp.GenerateStructure

/**
 * Union of every opcode used by the assembunny (2015/2016) and Duet (2017) machines.
 *
 * An instruction only describes what it does to the [AsmComputer] it runs on and returns the
 * program counter delta.
 */
@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed interface AsmInstruction {
    /** Runs this instruction against [computer] and returns how far the program counter should move. */
    fun execute(computer: AsmComputer): Int

    data class Inc(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] + 1L
            return AsmComputer.NEXT
        }
    }

    data class Hlf(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] / 2L
            return AsmComputer.NEXT
        }
    }

    data class Tpl(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] * 3L
            return AsmComputer.NEXT
        }
    }

    data class Jmp(
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = offset
    }

    data class Jie(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] % 2L ==
                0L
            ) {
                offset
            } else {
                AsmComputer.NEXT
            }
    }

    data class Jio(
        val register: String,
        val offset: Int,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = if (computer[register] == 1L) offset else AsmComputer.NEXT
    }

    data class Dec(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] - 1L
            return AsmComputer.NEXT
        }
    }

    data class Cpy(
        val valueOrRegister: String,
        val register2: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register2] = computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    data class Jnz(
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
    data class Tgl(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer.toggle(computer.value(register).toInt())
            return AsmComputer.NEXT
        }
    }

    /** Emits a value; only the days that own an output channel can run it. */
    data class Out(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = AsmComputer.NEXT
    }

    /** Plays a sound; only the days that own a sound card can run it. */
    data class Snd(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = AsmComputer.NEXT
    }

    data class Set(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    data class Add(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] + computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    data class Mul(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] * computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    data class Mod(
        val register: String,
        val valueOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int {
            computer[register] = computer[register] % computer.value(valueOrRegister)
            return AsmComputer.NEXT
        }
    }

    /** Recovers the last sound; only the days that own a sound card can run it. */
    data class Rcv(
        val register: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int = AsmComputer.NEXT
    }

    data class Jgz(
        val register: String,
        val offsetOrRegister: String,
    ) : AsmInstruction {
        override fun execute(computer: AsmComputer): Int =
            if (computer[register] > 0L) {
                computer.value(offsetOrRegister).toInt()
            } else {
                AsmComputer.NEXT
            }
    }
}
