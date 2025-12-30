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
            runDayParts(year, dayTask)
        }
        println()
    }

    private fun runDayParts(
        year: YearEnum,
        dayName: DayEnum,
    ) {
        val dayClassName = "aoc.${year.toString().replaceFirstChar(Char::lowercaseChar)}.$dayName"
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
