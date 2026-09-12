package aoc.year2017.entity

import aoc.common.entity.asm.AsmComputer
import aoc.common.entity.asm.AsmComputer.Companion.NEXT
import aoc.common.entity.asm.AsmComputer.Companion.WAIT
import aoc.common.entity.asm.AsmInstruction

/**
 * Duet machine as 2017 day 18 part one reads the tablet: `snd` plays a sound and `rcv` recovers the
 * last one played, but only when the register it names is not zero.
 *
 * The puzzle asks for the first recovered frequency, so the machine stops itself the moment a
 * recovery happens and exposes the value through [recovered].
 */
class SoloDuetComputer(
    program: List<AsmInstruction>,
) : AsmComputer(program) {
    private var lastSound = 0L

    /** Frequency recovered by the first effective `rcv`, or null while none happened. */
    var recovered: Long? = null
        private set

    override fun send(sent: Long) {
        lastSound = sent
    }

    override fun recover(register: String): Int {
        if (this[register] != 0L) {
            recovered = lastSound
            halt()
        }
        return NEXT
    }
}

/**
 * Duet machine as 2017 day 18 part two reads the tablet: two copies of the program run side by
 * side, `snd` queues a message for the partner and `rcv` takes one from the own queue, waiting when
 * it is empty.
 *
 * Waiting is expressed by blocking and returning a program counter delta of 0, so the very same
 * `rcv` runs again once the partner delivers. That keeps the scheduler in [runDuet] trivial: give
 * each machine the floor until it cannot move, and stop when neither moved.
 *
 * @param programId value the `p` register starts at, and what tells the two copies apart
 */
class PairedDuetComputer(
    program: List<AsmInstruction>,
    programId: Long,
) : AsmComputer(program, mapOf(P_REG to programId)) {
    private val inbox = ArrayDeque<Long>()

    /** Amount of values this machine sent to its partner. */
    var sendCount: Long = 0L
        private set

    lateinit var partner: PairedDuetComputer

    override fun send(sent: Long) {
        partner.inbox.addLast(sent)
        partner.unblock()
        sendCount++
    }

    override fun recover(register: String): Int {
        val message = inbox.removeFirstOrNull()
        if (message == null) {
            block()
            return WAIT
        }
        this[register] = message
        return NEXT
    }

    companion object {
        /**
         * Runs two copies of [program] against each other until both are stuck, either because they
         * ran off the end or because each is waiting for a message the other will never send.
         *
         * @return how many values program 1 sent, the answer to 2017 day 18 part two
         */
        fun runDuet(program: List<AsmInstruction>): Long {
            val programZero = PairedDuetComputer(program, 0L)
            val programOne = PairedDuetComputer(program, 1L)
            programZero.partner = programOne
            programOne.partner = programZero

            // `or` rather than `||`: both machines must get a turn before progress is judged.
            while (programZero.drain() or programOne.drain()) {
                // Nothing to do, the machines make the progress.
            }
            return programOne.sendCount
        }
    }
}
