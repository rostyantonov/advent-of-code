package aoc.common.parallel

import aoc.common.IAoCDay
import aoc.common.launcher.DayEnum
import aoc.common.launcher.YearEnum
import aoc.common.util.formatted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.reflect.full.createInstance
import kotlin.system.measureTimeMillis

/**
 * Parallel execution utilities for running multiple Advent of Code days concurrently.
 * Useful for running all days of a year or all years in parallel to get results faster.
 */
object ParallelRunner {
    /**
     * Result of running a day's solution.
     */
    data class DayResult(
        val year: YearEnum,
        val day: DayEnum,
        val partOne: Any?,
        val partTwo: Any?,
        val executionTimeMs: Long,
        val error: Exception? = null,
    )

    /**
     * Run all implemented days of a year in parallel.
     * @param year Year to run
     * @param maxConcurrency Maximum number of days to run concurrently (default: number of processors)
     * @return List of results for each day
     */
    fun runYearParallel(
        year: YearEnum,
        maxConcurrency: Int = Runtime.getRuntime().availableProcessors(),
    ): List<DayResult> =
        runBlocking(Dispatchers.Default.limitedParallelism(maxConcurrency)) {
            DayEnum.entries.map { day ->
                async { runDay(year, day) }
            }.awaitAll().filterNotNull()
        }

    /**
     * Run all implemented years in parallel.
     * @param maxConcurrency Maximum number of concurrent executions
     * @return Map of year to list of day results
     */
    fun runAllYearsParallel(maxConcurrency: Int = Runtime.getRuntime().availableProcessors()): Map<YearEnum, List<DayResult>> =
        runBlocking(Dispatchers.Default.limitedParallelism(maxConcurrency)) {
            YearEnum.entries.associate { year ->
                year to async { runYearParallel(year, maxConcurrency) }.await()
            }
        }

    /**
     * Run a single day and return the result.
     */
    private fun runDay(
        year: YearEnum,
        day: DayEnum,
    ): DayResult? {
        val dayClassName = "aoc.${year.toString().replaceFirstChar(Char::lowercaseChar)}.$day"

        return try {
            val dayInstance = Class.forName(dayClassName).kotlin.createInstance() as IAoCDay<*>

            var partOneResult: Any? = null
            var partTwoResult: Any? = null

            val executionTime =
                measureTimeMillis {
                    partOneResult = dayInstance.processPartOne()
                    partTwoResult = dayInstance.processPartTwo()
                }

            DayResult(
                year = year,
                day = day,
                partOne = partOneResult,
                partTwo = partTwoResult,
                executionTimeMs = executionTime,
            )
        } catch (_: ClassNotFoundException) {
            // Day not implemented - skip
            null
        } catch (e: Exception) {
            DayResult(
                year = year,
                day = day,
                partOne = null,
                partTwo = null,
                executionTimeMs = 0,
                error = e,
            )
        }
    }

    /**
     * Print results in a formatted way.
     */
    fun printResults(results: List<DayResult>) {
        results
            .sortedBy { it.day }
            .forEach { result ->
                if (result.error != null) {
                    println("${result.year}/${result.day}: ERROR - ${result.error.message}")
                } else {
                    println(
                        "${result.year}/${result.day}: " +
                            "Part 1 = ${formatResult(result.partOne)}, " +
                            "Part 2 = ${formatResult(result.partTwo)} " +
                            "(${result.executionTimeMs}ms)",
                    )
                }
            }
    }

    /**
     * Print summary statistics.
     */
    fun printSummary(results: Map<YearEnum, List<DayResult>>) {
        println("\n=== Parallel Execution Summary ===")
        results.forEach { (year, dayResults) ->
            val implemented = dayResults.size
            val errors = dayResults.count { it.error != null }
            val totalTime = dayResults.sumOf { it.executionTimeMs }
            println(
                "$year: $implemented days implemented, $errors errors, " +
                    "total time: ${totalTime.formatted()}ms",
            )
        }
        val grandTotal = results.values.flatten().sumOf { it.executionTimeMs }
        println("Grand total execution time: ${grandTotal.formatted()}ms")
        println("=================================\n")
    }

    private fun formatResult(result: Any?): String =
        when (result) {
            is Number -> result.formatted()
            else -> result.toString()
        }
}
