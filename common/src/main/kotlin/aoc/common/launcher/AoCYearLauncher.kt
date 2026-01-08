package aoc.common.launcher

import aoc.common.IAoCDay
import aoc.common.util.formatted
import kotlin.reflect.full.createInstance

object AoCYearLauncher {
    fun runAllYears() {
        YearEnum.entries.forEach(::runYear)
    }

    fun runYear(year: YearEnum) {
        println("Running year $year.")
        DayEnum.entries.forEach { dayTask ->
            if (year <= YearEnum.YEAR_2025 || dayTask <= DayEnum.DAY_12) {
                runDayParts(year, dayTask)
            }
        }
        println()
    }

    private fun runDayParts(
        year: YearEnum,
        dayName: DayEnum,
    ) {
        val dayClassName = "aoc.${year.yearName}.${dayName.dayName}"
        try {
            val daySelected = Class.forName(dayClassName).kotlin.createInstance() as IAoCDay<*>
            printInfo(
                year = year,
                dayName = dayName,
                daySelected.processPartOne(),
            )
            printInfo(
                year = year,
                dayName = dayName,
                daySelected.processPartTwo(),
            )
        } catch (_: ClassNotFoundException) {
            // Day not implemented yet - this is expected
        } catch (e: Exception) {
            println("ERROR: $year/$dayName failed with: ${e.javaClass.simpleName}: ${e.message}")
            if (System.getProperty("aoc.debug") == "true") {
                e.printStackTrace()
            }
        }
    }

    private fun printInfo(
        year: YearEnum,
        dayName: DayEnum,
        result: Any?,
    ) {
        if (result is Number) {
            printInfo(year, dayName, result)
        } else {
            println("Date: $year/$dayName -> result: $result")
        }
    }

    private fun printInfo(
        year: YearEnum,
        dayName: DayEnum,
        result: Number,
    ) {
        println("Date: $year/$dayName -> result: ${result.formatted()}")
    }
}
