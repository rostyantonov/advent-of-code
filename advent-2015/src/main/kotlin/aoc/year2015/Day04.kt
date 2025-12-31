package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StringInput
import java.security.MessageDigest
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class Day04 : AoCFileInput<String, Int>() {
    override var rawInput = listOf("yzbqklnj")

    override val inputFunction
        get() = StringInput::firstString

    /**
     * Santa needs help mining some AdventCoins (very similar to bitcoins)
     * to use as gifts for all the economically forward-thinking little girls and boys.
     *
     * To do this, he needs to find MD5 hashes which, in hexadecimal, start with at least five zeroes.
     * The input to the MD5 hash is some secret key (your puzzle input, given below)
     * followed by a number in decimal. To mine AdventCoins,
     * you must find Santa the lowest positive number (no leading zeroes: 1, 2, 3, ...) that produces such a hash.
     *
     * For example:
     *
     *      If your secret key is abcdef, the answer is 609043, because the MD5 hash of abcdef609043
     *        starts with five zeroes (000001dbbfa...), and it is the lowest such number to do so.
     *      If your secret key is pqrstuv, the lowest number it combines with to make an MD5 hash
     *        starting with five zeroes is 1048970; that is, the MD5 hash of pqrstuv1048970 looks like 000006136ef....
     */
    override fun processPartOne(): Int = searchTillParallel("00000")
    // result 282 749 for part 1

    // Now find one that starts with six zeroes.
    override fun processPartTwo(): Int = searchTillParallel("000000")
    // result 9 962 624 for part 2

    private fun searchTillParallel(startString: String): Int {
        val numThreads = Runtime.getRuntime().availableProcessors()
        val chunkSize = 10_000
        val result = AtomicInteger(Int.MAX_VALUE)

        val executor = Executors.newFixedThreadPool(numThreads)

        try {
            val futures =
                (0 until numThreads).map { threadId ->
                    executor.submit {
                        val md = MessageDigest.getInstance("MD5")
                        var start = threadId * chunkSize

                        while (start < result.get()) {
                            val end = minOf(start + chunkSize, result.get())

                            for (i in start until end) {
                                if (i >= result.get()) break

                                val hash =
                                    md
                                        .digest("$input$i".toByteArray())
                                        .toHexString()

                                if (hash.startsWith(startString)) {
                                    var current: Int
                                    do {
                                        current = result.get()
                                        if (i >= current) break
                                    } while (!result.compareAndSet(current, i))
                                    return@submit
                                }
                            }

                            start += numThreads * chunkSize
                        }
                    }
                }

            futures.forEach { it.get() }
        } finally {
            executor.shutdown()
        }

        return result.get()
    }
}
