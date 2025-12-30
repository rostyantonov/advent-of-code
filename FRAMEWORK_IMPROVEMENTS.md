# Framework Improvements - Usage Examples

This document provides practical examples for using the framework enhancements added to the Advent of Code project.

## Table of Contents

1. [Performance Monitoring](#1-performance-monitoring)
2. [Input Caching](#2-input-caching)
3. [Enhanced Error Handling](#3-enhanced-error-handling)
4. [Resource Management](#4-resource-management)
5. [Result Validation](#5-result-validation)
6. [Test Data Management DSL](#6-test-data-management-dsl)
7. [Parallel Execution](#7-parallel-execution)

---

## 1. Performance Monitoring

The `PerformanceMonitor` utility helps benchmark your solution's execution time with detailed statistics.

### Basic Usage

```kotlin
import aoc.common.performance.PerformanceMonitor

class Day01 : IAoCDay {
    override fun part1(input: String): Any {
        val result = PerformanceMonitor.measure("Day 01 - Part 1") {
            // Your solution logic here
            input.lines().sumOf { it.toInt() }
        }
        return result
    }
}
```

### Advanced Usage with Multiple Iterations

```kotlin
import aoc.common.performance.PerformanceMonitor
import aoc.common.performance.PerformanceStats

fun benchmarkSolution() {
    val stats = PerformanceMonitor.benchmark(
        name = "Day 01 - Part 1",
        iterations = 100,
        warmupIterations = 10
    ) {
        // Your solution logic
        calculateResult()
    }
    
    println("Min: ${stats.minTime}ms")
    println("Max: ${stats.maxTime}ms")
    println("Average: ${stats.avgTime}ms")
    println("Median: ${stats.medianTime}ms")
}
```

### Comparing Multiple Approaches

```kotlin
fun compareAlgorithms() {
    val approach1Stats = PerformanceMonitor.benchmark("Approach 1", 50) {
        bruteForceApproach()
    }
    
    val approach2Stats = PerformanceMonitor.benchmark("Approach 2", 50) {
        optimizedApproach()
    }
    
    PerformanceMonitor.printComparison(approach1Stats, approach2Stats)
}
```

---

## 2. Input Caching

The `InputCache` system caches parsed input to avoid redundant processing during multiple test runs.

### Basic Usage

```kotlin
import aoc.common.cache.InputCache

class Day01 : IAoCDay {
    private val cache = InputCache<List<Int>>()
    
    override fun part1(input: String): Any {
        val numbers = cache.getOrPut("day01-numbers") {
            // This parsing happens only once
            input.lines().map { it.toInt() }
        }
        return numbers.sum()
    }
}
```

### With Custom TTL (Time-to-Live)

```kotlin
import aoc.common.cache.InputCache
import kotlin.time.Duration.Companion.minutes

class Day01 : IAoCDay {
    // Cache expires after 5 minutes
    private val cache = InputCache<List<Int>>(ttl = 5.minutes)
    
    override fun part1(input: String): Any {
        val numbers = cache.getOrPut("day01-numbers") {
            input.lines().map { it.toInt() }
        }
        return numbers.sum()
    }
}
```

### Advanced: Conditional Caching

```kotlin
fun processInputWithCache(input: String, useCache: Boolean): List<Int> {
    if (!useCache) {
        return input.lines().map { it.toInt() }
    }
    
    return InputCache<List<Int>>().getOrPut("numbers") {
        input.lines().map { it.toInt() }
    }
}
```

### Cache Management

```kotlin
val cache = InputCache<List<Int>>()

// Clear specific entry
cache.invalidate("day01-numbers")

// Clear all entries
cache.clear()

// Check if cached
if (cache.contains("day01-numbers")) {
    println("Data is cached")
}
```

---

## 3. Enhanced Error Handling

The enhanced `AoCYearLauncher` provides better error messages and debug mode.

### Basic Usage

```kotlin
import aoc.common.launcher.AoCYearLauncher
import aoc.common.launcher.DayEnum
import aoc.common.launcher.YearEnum

fun main() {
    try {
        AoCYearLauncher.launchDay(YearEnum.Y2015, DayEnum.D01)
    } catch (e: Exception) {
        // Enhanced error messages with context
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}
```

### With Debug Mode

```kotlin
// Enable debug mode for verbose logging
System.setProperty("aoc.debug", "true")

fun main() {
    // Now launches with detailed logging
    AoCYearLauncher.launchDay(YearEnum.Y2015, DayEnum.D01)
}
```

### Custom Error Handling

```kotlin
fun safeLaunchDay(year: YearEnum, day: DayEnum) {
    try {
        AoCYearLauncher.launchDay(year, day)
    } catch (e: ClassNotFoundException) {
        println("Day ${day.name} not implemented for ${year.name}")
        println("Suggestion: Create class ${year.name}.${day.name}")
    } catch (e: Exception) {
        println("Unexpected error: ${e.message}")
        if (System.getProperty("aoc.debug") == "true") {
            e.printStackTrace()
        }
    }
}
```

---

## 4. Resource Management

The `ResourceReader` utility simplifies reading input files from classpath or filesystem.

### Reading from Classpath Resources

```kotlin
import aoc.common.resource.ResourceReader

class Day01 : IAoCDay {
    override fun part1(input: String): Any {
        // Read from src/main/resources/2015/day01.txt
        val text = ResourceReader.readResourceAsText("2015/day01.txt")
        return processInput(text)
    }
}
```

### Reading as Lines

```kotlin
import aoc.common.resource.ResourceReader

fun loadInstructions(): List<String> {
    return ResourceReader.readResourceAsLines("2015/day01.txt")
}
```

### Reading from File System

```kotlin
import aoc.common.resource.ResourceReader
import java.nio.file.Paths

fun loadFromFile() {
    val path = Paths.get("inputs/2015/day01.txt")
    val content = ResourceReader.readFileAsText(path)
    println(content)
}
```

### Reading Binary Data

```kotlin
import aoc.common.resource.ResourceReader

fun loadBinaryData(): ByteArray {
    return ResourceReader.readResourceAsBytes("2015/day01.dat")
}
```

### With Error Handling

```kotlin
fun safeReadResource(path: String): String? {
    return try {
        ResourceReader.readResourceAsText(path)
    } catch (e: Exception) {
        println("Failed to read resource: ${e.message}")
        null
    }
}
```

---

## 5. Result Validation

The `ResultValidator` helps validate solution outputs with detailed error messages.

### Basic Type Validation

```kotlin
import aoc.common.validation.ResultValidator

class Day01Test {
    @Test
    fun testPart1() {
        val result = day01.part1(sampleInput)
        
        // Validates result is an Int
        ResultValidator.validateInt(result)
        
        assertEquals(42, result)
    }
}
```

### Range Validation

```kotlin
import aoc.common.validation.ResultValidator

fun validateResult(result: Any) {
    // Ensure result is between 0 and 1000
    ResultValidator.validateRange(result, 0L, 1000L)
}
```

### Custom Validation Rules

```kotlin
import aoc.common.validation.ResultValidator

fun validateCustomRule(result: Any) {
    ResultValidator.validate(result, "Result must be even") { value ->
        value is Int && value % 2 == 0
    }
}
```

### Multiple Validations

```kotlin
fun validateSolution(result: Any) {
    // Chain multiple validations
    ResultValidator.validateLong(result)
    ResultValidator.validateRange(result, 1L, 1_000_000L)
    ResultValidator.validate(result, "Must be positive") { it is Long && it > 0 }
}
```

### In Test Cases

```kotlin
class Day01Test {
    @Test
    fun testPart1WithValidation() {
        val result = day01.part1(sampleInput)
        
        // Comprehensive validation
        ResultValidator.validateInt(result)
        ResultValidator.validateRange(result, 0L, 100L)
        
        // Custom business rule
        ResultValidator.validate(result, "Result should be divisible by 3") {
            it is Int && it % 3 == 0
        }
        
        assertEquals(42, result)
    }
}
```

---

## 6. Test Data Management DSL

The Test Data DSL provides a readable way to define test cases.

### Basic Test Case Definition

```kotlin
import aoc.common.dsl.*

class Day01Test {
    @Test
    fun testPart1Examples() {
        val testCases = testData {
            case("Simple case") {
                input = "123"
                expected = 6
            }
            
            case("Empty input") {
                input = ""
                expected = 0
            }
            
            case("Negative numbers") {
                input = "-1\n-2\n-3"
                expected = -6
            }
        }
        
        testCases.forEach { testCase ->
            val result = day01.part1(testCase.input)
            assertEquals(testCase.expected, result, testCase.description)
        }
    }
}
```

### With Parameterized Tests

```kotlin
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import aoc.common.dsl.*

class Day01Test {
    companion object {
        @JvmStatic
        fun part1TestCases() = testData {
            case("Example 1") {
                input = "()())"
                expected = -1
            }
            
            case("Example 2") {
                input = "((("
                expected = 3
            }
            
            case("Example 3") {
                input = ")))"
                expected = -3
            }
        }.toList()
    }
    
    @ParameterizedTest
    @MethodSource("part1TestCases")
    fun testPart1(testCase: TestCase) {
        val result = day01.part1(testCase.input)
        assertEquals(testCase.expected, result, testCase.description)
    }
}
```

### Complex Test Scenarios

```kotlin
val advancedTests = testData {
    case("Large dataset") {
        input = generateLargeInput(10000)
        expected = 50005000
        metadata = mapOf(
            "size" to 10000,
            "type" to "performance"
        )
    }
    
    case("Edge case: overflow") {
        input = Long.MAX_VALUE.toString()
        expected = Long.MAX_VALUE
        metadata = mapOf("type" to "boundary")
    }
}
```

### Reusable Test Data

```kotlin
object TestDataSets {
    val basicTests = testData {
        case("Zero") { input = "0"; expected = 0 }
        case("One") { input = "1"; expected = 1 }
        case("Ten") { input = "10"; expected = 10 }
    }
    
    val edgeCases = testData {
        case("Min Int") { input = Int.MIN_VALUE.toString(); expected = Int.MIN_VALUE }
        case("Max Int") { input = Int.MAX_VALUE.toString(); expected = Int.MAX_VALUE }
    }
}

class Day01Test {
    @Test
    fun runBasicTests() {
        TestDataSets.basicTests.forEach { testCase ->
            val result = day01.part1(testCase.input)
            assertEquals(testCase.expected, result)
        }
    }
}
```

---

## 7. Parallel Execution

The `ParallelRunner` enables concurrent execution of multiple days for faster processing.

### Running Multiple Days in Parallel

```kotlin
import aoc.common.parallel.ParallelRunner
import aoc.common.launcher.DayEnum
import aoc.common.launcher.YearEnum

fun main() {
    val days = listOf(DayEnum.D01, DayEnum.D02, DayEnum.D03)
    
    val results = ParallelRunner.runDaysInParallel(
        year = YearEnum.Y2015,
        days = days
    )
    
    results.forEach { (day, result) ->
        println("$day: ${result.getOrNull()}")
    }
}
```

### With Custom Thread Pool

```kotlin
import aoc.common.parallel.ParallelRunner

fun runWithCustomThreads() {
    val runner = ParallelRunner(maxThreads = 4)
    
    val days = listOf(DayEnum.D01, DayEnum.D02, DayEnum.D03, DayEnum.D04)
    val results = runner.runDaysInParallel(YearEnum.Y2015, days)
    
    // Process results
    val successful = results.count { it.second.isSuccess }
    println("Completed: $successful/${results.size}")
}
```

### Running All Days of a Year

```kotlin
fun runEntireYear(year: YearEnum) {
    val allDays = DayEnum.values().toList()
    
    val results = ParallelRunner.runDaysInParallel(year, allDays)
    
    // Aggregate results
    val totalTime = results.sumOf { (_, result) ->
        result.getOrNull()?.executionTime ?: 0L
    }
    
    println("Total execution time: ${totalTime}ms")
}
```

### With Error Handling

```kotlin
fun safeParallelRun(year: YearEnum, days: List<DayEnum>) {
    val results = ParallelRunner.runDaysInParallel(year, days)
    
    // Separate successful and failed runs
    val (successful, failed) = results.partition { it.second.isSuccess }
    
    println("Successful: ${successful.size}")
    successful.forEach { (day, result) ->
        println("  $day: ${result.getOrNull()}")
    }
    
    if (failed.isNotEmpty()) {
        println("\nFailed: ${failed.size}")
        failed.forEach { (day, result) ->
            println("  $day: ${result.exceptionOrNull()?.message}")
        }
    }
}
```

### Collecting Performance Metrics

```kotlin
import aoc.common.parallel.ParallelRunner
import aoc.common.performance.PerformanceMonitor

fun benchmarkAllDays(year: YearEnum) {
    val allDays = DayEnum.values().toList()
    
    val overallStats = PerformanceMonitor.benchmark("All Days", iterations = 1) {
        ParallelRunner.runDaysInParallel(year, allDays)
    }
    
    println("Total time for all days: ${overallStats.avgTime}ms")
}
```

### Custom Task Execution

```kotlin
fun runCustomTasks() {
    val runner = ParallelRunner(maxThreads = 8)
    
    val tasks = (1..25).map { day ->
        {
            // Custom processing logic
            println("Processing day $day")
            Thread.sleep(100)
            "Day $day completed"
        }
    }
    
    runner.runTasks(tasks).forEach { result ->
        println(result.getOrNull())
    }
}
```

---

## Combined Example: Complete Solution

Here's a complete example using multiple framework improvements together:

```kotlin
import aoc.common.*
import aoc.common.cache.InputCache
import aoc.common.performance.PerformanceMonitor
import aoc.common.resource.ResourceReader
import aoc.common.validation.ResultValidator
import aoc.common.dsl.*

class Day01(private val useCache: Boolean = true) : IAoCDay {
    private val cache = InputCache<List<Int>>()
    
    override fun part1(input: String): Any {
        return PerformanceMonitor.measure("Day 01 - Part 1") {
            val numbers = if (useCache) {
                cache.getOrPut("day01-part1") {
                    parseInput(input)
                }
            } else {
                parseInput(input)
            }
            
            val result = numbers.sum()
            
            // Validate result
            ResultValidator.validateInt(result)
            ResultValidator.validateRange(result, 0L, 1_000_000L)
            
            result
        }
    }
    
    private fun parseInput(input: String): List<Int> {
        return input.lines().map { it.trim().toInt() }
    }
}

class Day01Test {
    private val day01 = Day01()
    
    companion object {
        @JvmStatic
        fun testCases() = testData {
            case("Example 1") {
                input = "1\n2\n3"
                expected = 6
            }
            case("Example 2") {
                input = "10\n20\n30"
                expected = 60
            }
            case("Single number") {
                input = "42"
                expected = 42
            }
        }.toList()
    }
    
    @ParameterizedTest
    @MethodSource("testCases")
    fun testPart1(testCase: TestCase) {
        val result = day01.part1(testCase.input)
        assertEquals(testCase.expected, result, testCase.description)
    }
    
    @Test
    fun testPart1WithRealInput() {
        val input = ResourceReader.readResourceAsText("2015/day01.txt")
        val result = day01.part1(input)
        
        ResultValidator.validateInt(result)
        assertTrue(result is Int && result > 0)
    }
}

// Running the solution
fun main() {
    // Run single day
    AoCYearLauncher.launchDay(YearEnum.Y2015, DayEnum.D01)
    
    // Run multiple days in parallel
    val days = listOf(DayEnum.D01, DayEnum.D02, DayEnum.D03)
    val results = ParallelRunner.runDaysInParallel(YearEnum.Y2015, days)
    
    results.forEach { (day, result) ->
        println("$day: ${result.getOrNull()}")
    }
}
```

---

## Best Practices

1. **Performance Monitoring**: Use for optimization, not in production runs
2. **Input Caching**: Enable for test suites, disable for benchmarking
3. **Error Handling**: Always enable debug mode during development
4. **Resource Management**: Use `ResourceReader` for consistent input loading
5. **Result Validation**: Include in tests to catch regressions early
6. **Test Data DSL**: Keep test cases close to the tests for readability
7. **Parallel Execution**: Use for running multiple days, not within a single day's logic

---

## Performance Tips

- **Caching**: Can improve test suite performance by 10-100x for complex parsing
- **Parallel Execution**: Expect 2-4x speedup on modern multi-core processors
- **Performance Monitoring**: Use warmup iterations for accurate JVM benchmarking
- **Resource Management**: Prefer `ResourceReader` over manual `File` operations

---

## Troubleshooting

### Cache Not Working
```kotlin
// Clear cache if stale data is suspected
InputCache<List<Int>>().clear()
```

### Parallel Execution Hangs
```kotlin
// Reduce thread count or increase timeout
val runner = ParallelRunner(maxThreads = 2)
```

### Resource Not Found
```kotlin
// Verify resource path relative to src/main/resources/
val path = "2015/day01.txt"  // Not "/2015/day01.txt"
```

---

For more information, see the source code in `common/src/main/kotlin/aoc/common/`.
