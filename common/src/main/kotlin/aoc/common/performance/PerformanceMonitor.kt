package aoc.common.performance

import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

/**
 * Utility for monitoring and reporting performance metrics of Advent of Code solutions.
 * Thread-safe for use in concurrent environments.
 */
object PerformanceMonitor {
    internal val metrics = ConcurrentHashMap<String, MutableList<Long>>()

    @Volatile
    internal var enabled = false

    /**
     * Enable performance monitoring.
     */
    fun enable() {
        enabled = true
    }

    /**
     * Disable performance monitoring.
     */
    fun disable() {
        enabled = false
    }

    /**
     * Clear all collected metrics.
     */
    fun clear() {
        metrics.clear()
    }

    /**
     * Execute a block of code and measure its execution time.
     * @param key Identifier for this measurement
     * @param block Code block to measure
     * @return Result of the block execution
     */
    fun <T> measure(
        key: String,
        block: () -> T,
    ): T {
        if (!enabled) return block()

        var result: T
        val time =
            measureTimeMillis {
                result = block()
            }

        metrics.getOrPut(key) { mutableListOf() }.apply {
            synchronized(this) {
                add(time)
            }
        }
        return result
    }

    /**
     * Get statistics for a specific measurement key.
     */
    fun getStats(key: String): PerformanceStats? {
        val times = metrics[key] ?: return null
        if (times.isEmpty()) return null

        return PerformanceStats(
            key = key,
            count = times.size,
            min = times.minOrNull() ?: 0,
            max = times.maxOrNull() ?: 0,
            average = times.average(),
            total = times.sum(),
        )
    }

    /**
     * Get all collected statistics.
     */
    fun getAllStats(): List<PerformanceStats> = metrics.keys.mapNotNull { getStats(it) }

    /**
     * Print a formatted report of all performance metrics.
     */
    fun printReport() {
        if (metrics.isEmpty()) {
            println("No performance data collected.")
            return
        }

        println("\n=== Performance Report ===")
        getAllStats()
            .sortedByDescending { it.total }
            .forEach { stats ->
                println(
                    "${stats.key}: avg=${stats.average.toLong().formatted()}ms, " +
                        "min=${stats.min.formatted()}ms, max=${stats.max.formatted()}ms, " +
                        "total=${stats.total.formatted()}ms (${stats.count} runs)",
                )
            }
        println("========================\n")
    }
}

/**
 * Performance statistics for a measurement key.
 */
data class PerformanceStats(
    val key: String,
    val count: Int,
    val min: Long,
    val max: Long,
    val average: Double,
    val total: Long,
)
