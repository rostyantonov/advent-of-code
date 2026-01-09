# advent-of-code
Advent of Code solutions for all years starting 2015 on Kotlin

## Project Structure

This is a multi-module Gradle project with the following structure:

### Modules

#### 1. ksp-processor
Kotlin Symbol Processing (KSP) annotation processor module that provides:
- `@AdventSolution` annotation for marking solution classes
- Automatic generation of a solution registry
- Build-time code generation for better type safety

**Key Files:**
- `AdventSolution.kt` - Annotation to mark solution classes
- `AdventSolutionProcessor.kt` - KSP processor implementation
- `AdventSolutionProcessorProvider.kt` - KSP provider

#### 2. common
Shared utilities and base classes used across all solution modules:
- `Solution` interface - Base interface for all solution implementations
- `InputReader` - Utility for reading puzzle input files
- `Utils` - Common helper functions for puzzles (grid operations, parsing, etc.)

**Features:**
- Grid navigation helpers (4 and 8-directional neighbors)
- Manhattan distance calculation
- String to number list conversions
- Matrix transposition
- Input file reading utilities

#### 3. advent-2015
Solutions for Advent of Code 2015 puzzles:
- Each day is implemented as a separate class (Day01, Day02, etc.)
- All solutions are annotated with `@AdventSolution`
- Solutions implement the `Solution` interface from the common module

**Example Solutions:**
- Day 1: Not Quite Lisp - Floor navigation
- Day 2: I Was Told There Would Be No Math - Wrapping paper calculation

#### 4. advent-2016
Solutions for Advent of Code 2016 puzzles:
- Same structure as advent-2015
- Each day implements the `Solution` interface

**Example Solutions:**
- Day 1: No Time for a Taxicab - Grid navigation

## Building and Running

### Build the project
```bash
./gradlew build
```

### Run tests
```bash
./gradlew test
```

### Run tests for a specific module
```bash
./gradlew :advent-2015:test
./gradlew :advent-2016:test
```

## Adding New Solutions

1. Create a new class in the appropriate year module (e.g., `advent-2015/src/main/kotlin/advent/of/code/year2015/Day03.kt`)
2. Implement the `Solution` interface
3. Annotate the class with `@AdventSolution(year = YYYY, day = DD)`
4. Add your puzzle input to `src/main/resources/inputs/YYYY/dayDD.txt`
5. Create tests in the corresponding test directory

Example:
```kotlin
@AdventSolution(year = 2015, day = 3)
class Day03 : Solution {
    override fun solvePart1(input: String): String {
        // Your solution here
    }

    override fun solvePart2(input: String): String {
        // Your solution here
    }
}
```

## Technology Stack

- **Kotlin**: 2.3.0
- **Java Toolchain**: 21
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JUnit 5
- **Code Generation**: KSP (Kotlin Symbol Processing)
- **Libraries**: KotlinPoet for code generation

## Module Dependencies

```
ksp-processor (standalone)
    ↓
common (depends on ksp-processor)
    ↓
advent-2015 (depends on common, uses ksp-processor for code generation)
advent-2016 (depends on common, uses ksp-processor for code generation)
```
