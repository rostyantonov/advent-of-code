# KSP Structure Generator Framework

This framework provides automatic code generation for entity classes that need to parse structured input using regex patterns.

## Overview

The `@GenerateStructure` annotation triggers KSP (Kotlin Symbol Processing) to automatically generate companion objects implementing `IStructure`, `IStructureLine`, `IStructureCustomLine`, or `IStructureMulti` for your data classes. This eliminates boilerplate code for parsing regex-matched input.

## Features

- **Automatic Implementation Generation**: Annotate your data class and get a complete companion object
- **Multiple Patterns**: Supports standard, line-based, custom line, and multi-structure patterns
- **Type Support**: Supports Int, String, Char (nullable and non-nullable)
- **Field Name Mapping**: Automatically maps regex named groups to field names
- **Extensible**: Easy to add support for additional types in `BaseEntity`

## Generation Modes

### Standard Mode (IStructure)
Parses a single match per line and extracts fields from named groups.

```kotlin
@GenerateStructure
data class Person(
    val name: String,
    val age: Int,
)
```

### Line-Based Mode (IStructureLine) 
Finds all regex matches in a line and creates a list of entities. Useful for parsing multiple occurrences in one line.

```kotlin
@GenerateStructure(lineBased = true)
data class WalkerInstruction(
    val direction: Char,
    val steps: Int,
)
// Parses "R3, L5, R2" -> List(WalkerInstruction('R', 3), WalkerInstruction('L', 5), WalkerInstruction('R', 2))
```

### Custom Line Mode (IStructureCustomLine)
Processes the entire line and all match results, allowing for custom parsing logic.

```kotlin
@GenerateStructure(customLine = true)
data class Molecule(
    val stringValue: String,
    val atoms: List<Atom>,
)
```

### Multi-Structure Mode (IStructureMulti)
Routes to different sealed subclasses based on a discriminator field.

```kotlin
@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed class AsmInstruction {
    data class Jmp(val offset: Int) : AsmInstruction()
    data class Inc(val register: String) : AsmInstruction()
}
```

## Usage

### 1. Annotate Your Data Class

```kotlin
import aoc.ksp.GenerateStructure

@GenerateStructure
data class Person(
    val name: String,
    val age: Int,
    val score: Int,
) {
    companion object  // Required for extension functions
}
```

### 2. Use the Generated Code

The framework generates a standalone companion object:

**Standard Mode:**
```kotlin
// Define your regex with named groups matching the field names
val regex = Regex("(?<name>\\w+) age (?<age>\\d+) score (?<score>\\d+)")

// Use the standalone object directly
val person = PersonCompanion.fromLine("Jane age 30 score 200", regex)
```

**Line-Based Mode:**
```kotlin
// Define your regex - will be applied with findAll() to find all matches
val regex = Regex("(?<direction>[LR])(?<steps>\\d+)")

// Parse multiple instructions from a single line
val instructions = WalkerInstructionCompanion.fromLine("R3, L5, R2, L2", regex)
// Returns: List(WalkerInstruction('R', 3), WalkerInstruction('L', 5), ...)
```

### 3. Integration with StructuredInput

**Standard Mode:**
```kotlin
class MyDay : AoCFileInput<List<Person>, Int>() {
    override val inputFunction
        get() = StructuredInput(
            regex = Regex("(?<name>\\w+) age (?<age>\\d+) score (?<score>\\d+)"),
            builder = PersonCompanion::fromLine,
        )::getStructInput
    
    override fun processPartOne(): Int {
        return input.sumOf { it.score }
    }
}
```

**Line-Based Mode:**
```kotlin
class Day01 : AoCFileInput<List<WalkerInstruction>, Int>() {
    override val inputFunction
        get() = StructuredInput<List<WalkerInstruction>>(
            regex = Regex("(?<direction>[LR])(?<steps>\\d+)"),
            builder = WalkerInstructionCompanion::fromLine,
        )::getSingleStructInput
    
    override fun processPartOne(): Int {
        // input is already List<WalkerInstruction> from parsing the single line
        return input.sumOf { it.steps }
    }
}
```

## Supported Types

| Type | Non-Nullable | Nullable |
|------|--------------|----------|
| Int | ✅ | ✅ |
| String | ✅ | ✅ |
| Char | ✅ | ✅ |
| Custom* | ✅ | ✅ |

\* Custom types require a `@FieldConverter` annotation with a `TypeConverter` implementation.

## Custom Type Converters

For complex types not supported by default, you can implement custom converters using the `TypeConverter` interface.

### 1. Create a Type Converter

```kotlin
import aoc.ksp.TypeConverter
import aoc.ksp.BaseEntity
import kotlin.text.MatchGroupCollection

object PositionConverter : TypeConverter<Position> {
    override fun convert(
        collection: MatchGroupCollection,
        fieldName: String
    ): Position {
        // Extract Position from fields like "startX", "startY"
        val x = BaseEntity.getAsInt(collection, "${fieldName}X")
        val y = BaseEntity.getAsInt(collection, "${fieldName}Y")
        return Position(x, y)
    }
}
```

### 2. Use the Converter in Your Entity

```kotlin
import aoc.ksp.GenerateStructure
import aoc.ksp.FieldConverter

@GenerateStructure
data class Instruction(
    @FieldConverter(PositionConverter::class)
    val start: Position,
    @FieldConverter(PositionConverter::class)
    val end: Position,
    val action: String
)
```

### 3. Define Regex with Appropriate Groups

```kotlin
// Regex must have named groups that match the converter's expectations
val regex = Regex(
    "(?<startX>\\d+),(?<startY>\\d+) to (?<endX>\\d+),(?<endY>\\d+) (?<action>\\w+)"
)

// Usage
val instruction = InstructionCompanion.fromLine("0,0 to 999,999 toggle", regex)
// instruction.start == Position(0, 0)
// instruction.end == Position(999, 999)
// instruction.action == "toggle"
```

### Benefits of Custom Converters

- **Reusability**: Write the converter once, use it across multiple entities
- **Flexibility**: Handle complex parsing logic (nested structures, computations, validations)
- **Type Safety**: Compile-time checking of converter types
- **Encapsulation**: Keep parsing logic separate from entity classes

## How It Works

1. **Annotation Processing**: KSP scans for `@GenerateStructure` annotations at compile time
2. **Code Generation**: For each annotated class, generates:
   - A standalone companion object (`<ClassName>Companion`) implementing `IStructure<T>`
   - An extension function on the companion object for convenience
3. **Type Mapping**: Uses `BaseEntity` helper methods to convert regex groups to typed fields

## Generated Code Example

For the `Person` class above, KSP generates:

```kotlin
object PersonCompanion : IStructure<Person> {
    override fun create(collection: MatchGroupCollection): Person =
        Person(
            name = BaseEntity.getAsString(collection, "name"),
            age = BaseEntity.getAsInt(collection, "age"),
            score = BaseEntity.getAsInt(collection, "score"),
        )
}
```

## Adding New Type Support

To add support for a new type:

1. Add a getter pair in `BaseEntity.kt`, following the existing convention — the non-nullable
   getter delegates to the nullable one through the private `required` helper:
```kotlin
fun getAsYourType(collection: MatchGroupCollection, name: String) =
    required(name, "YourType", getAsNullableYourType(collection, name))

fun getAsNullableYourType(collection: MatchGroupCollection, name: String) =
    collection[name]?.value?.toYourTypeOrNull()
```

2. Add the simple name to the `supported` set in `getterExpression` in `StructureProcessor.kt`.
   The getter name is derived as `getAs<Type>` / `getAsNullable<Type>`, so no branch is needed —
   and because `getterExpression` is the single source of truth, standard entities, customLine
   entities and sealed subclass branches all pick up the new type at once.

## Limitations

- Classes with existing custom companion objects will be skipped (with a warning)
- Only primary constructor parameters are processed
- Regex named groups must match field names exactly

## Architecture

Two modules, both using package `aoc.ksp`. The split keeps `symbol-processing-api` off the
compile and runtime classpath of the solution modules — they only need the annotations and the
runtime interfaces.

```
ksp-annotations/                      # runtime API, no KSP dependency
└── src/main/kotlin/aoc/ksp/
    ├── GenerateStructure.kt          # Annotation definition
    ├── FieldConverter.kt             # Custom converter annotation
    ├── TypeConverter.kt              # Custom converter interface
    ├── BaseEntity.kt                 # Type conversion helpers
    ├── IStructure.kt                 # Base interface
    ├── IStructureLine.kt             # Line-based interface (findAll -> List<T>)
    ├── IStructureCustomLine.kt       # Custom line interface
    └── IStructureMulti.kt            # Multi-structure interface

ksp-processor/                        # compile-time only
├── src/main/kotlin/aoc/ksp/
│   └── StructureProcessor.kt         # KSP processor + provider
└── src/main/resources/META-INF/services/
    └── com.google.devtools.ksp.processing.SymbolProcessorProvider
```

## Build Configuration

A module that uses `@GenerateStructure` applies the KSP plugin and wires both halves:

```kotlin
plugins {
    id("aoc.kotlin-conventions")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":ksp-annotations"))
    ksp(project(":ksp-processor"))
}
```

See `common/build.gradle.kts` and `advent-2015/build.gradle.kts` for full examples. The shared
Kotlin/ktlint/detekt/test setup lives in the `aoc.kotlin-conventions` precompiled script plugin
under `buildSrc/`.
