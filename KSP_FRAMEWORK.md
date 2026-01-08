# KSP Structure Generator Framework

This framework provides automatic code generation for entity classes that need to parse structured input using regex patterns.

## Overview

The `@GenerateStructure` annotation triggers KSP (Kotlin Symbol Processing) to automatically generate companion objects implementing `IStructure`, `IStructureCustomLine`, or `IStructureMulti` for your data classes. This eliminates boilerplate code for parsing regex-matched input.

## Features

- **Automatic Implementation Generation**: Annotate your data class and get a complete companion object
- **Multiple Patterns**: Supports standard, custom line and multi-structure patterns
- **Type Support**: Supports Int, Long, Boolean, String, Char and any enum (nullable and
  non-nullable) in every mode
- **Field Name Mapping**: Automatically maps regex named groups to field names
- **Declared Input Window**: `skipHeaderLines`/`skipFooterLines` travel with the entity
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

### Match Sources (@FromMatch)
Parameters are read from the regex named group sharing their name. A parameter that has no such group
declares its source explicitly:

| `MatchPart` | Valid with | Parameter type | Value |
|-------------|-----------|----------------|-------|
| `LINE` | `customLine = true` | `String` | The whole input line |
| `ALL_MATCHES` | `customLine = true` | `List<T>` | Every match, each passed to `T(String)` |

Using a part outside `customLine`, or on the wrong parameter type, is a processor error rather than a
`TODO(...)` in the generated file.

### Custom Line Mode (IStructureCustomLine)
Processes the entire line and all match results, allowing for custom parsing logic. Neither source is
a named group, so every parameter says where it comes from with `@FromMatch` (see Match Sources above).

```kotlin
@GenerateStructure(customLine = true)
data class Molecule(
    @FromMatch(MatchPart.LINE) val stringValue: String,
    @FromMatch(MatchPart.ALL_MATCHES) val atoms: List<Atom>,
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
        get() =
            StructuredInput
                .of(
                    regex = Regex("(?<name>\\w+) age (?<age>\\d+) score (?<score>\\d+)"),
                    structure = PersonCompanion,
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
        get() =
            StructuredInput
                .of(
                    regex = Regex("(?<direction>[LR])(?<steps>\\d+)"),
                    structure = WalkerInstructionCompanion,
                )::getSingleStructInput
    
    override fun processPartOne(): Int {
        // input is already List<WalkerInstruction> from parsing the single line
        return input.sumOf { it.steps }
    }
}
```

### Skipping Header and Footer Lines

Declare the trimming on the entity and pass the companion itself; `StructuredInput.of` and
`StructuredMultiInput.of` read the counts off the generated companion, so no call site repeats them.

```kotlin
@GenerateStructure(skipHeaderLines = 2)
data class StorageNode(val x: Int, val y: Int, val size: Int, val used: Int, val avail: Int)

// Day22
StructuredInput.of(regex = Regex("..."), structure = StorageNodeCompanion)::getStructInput
```

The processor emits `override val skipHeaderLines` / `skipFooterLines` only for non-zero values. The
`StructuredInput(regex, builder, skipHeaderLines, skipFooterLines)` constructor stays available for
trimming that belongs to one puzzle rather than to the entity.

## Supported Types

| Type | Non-Nullable | Nullable |
|------|--------------|----------|
| Int | ✅ | ✅ |
| Long | ✅ | ✅ |
| Boolean** | ✅ | ✅ |
| String | ✅ | ✅ |
| Char | ✅ | ✅ |
| Enum*** | ✅ | ✅ |
| Custom* | ✅ | ✅ |

\* Custom types require a `@FieldConverter` annotation with a `TypeConverter` implementation.

\*\* Strictly `true` or `false`; anything else is a missing value rather than a silent `false`.

\*\*\* Any enum works without a converter. The group value is matched against the constant names
ignoring case, with spaces read as underscores (`turn on` -> `TURN_ON`). A `@FieldConverter` on an
enum field still wins, which is how `BitOperationConverter` keeps its `DIRECT` fallback for an
absent group.

The three generation modes support the same set of types. Anything else is a compile-time
error pointing you at `@FieldConverter` or `BaseEntity`.

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

2. Add the simple name to `SUPPORTED_TYPES` in `ParameterMappings.kt`. The getter name is derived
   as `getAs<Type>` / `getAsNullable<Type>`, so no branch is needed — and because
   `getterExpression` is the single source of truth, standard entities, customLine entities and
   sealed subclass branches all pick up the new type at once. `SUPPORTED_TYPES` also drives the
   "Supported field types" list in the generated KDoc, so the docs cannot drift from the check.

Enums need no step at all: `getAsEnum` is generic, so the processor recognises any
`ClassKind.ENUM_CLASS` parameter and imports the type when it lives outside the entity's package.

## Limitations

- Classes with existing custom companion objects will be skipped (with a warning)
- Only primary constructor parameters are processed
- Regex named groups must match field names exactly
- The annotated class must be top-level. The field-based modes need a concrete class;
  `multiStructure` needs a sealed class or sealed interface.

## Diagnostics

Every misuse is reported on the declaration that caused it rather than on a line in
`build/generated`, and every message below is covered by a test in
`ksp-processor/src/test/kotlin/aoc/ksp/StructureDiagnosticsTest.kt`.

- Two generation modes requested at once, or a blank/unused `discriminatorField`, or negative skips
- A target that is nested, abstract, not a class, or not sealed under `multiStructure`
- A type with no getter and no `@FieldConverter`
- A `@FieldConverter` whose `TypeConverter<T>` produces something other than the field's type
- `@FromMatch` outside `customLine`, or a part on a parameter of the wrong type
- `@FromMatch(ALL_MATCHES)` whose element type has no primary constructor taking a single `String`
- A `multiStructure` sealed class with no subclasses, a class with no primary constructor, or one
  with no parameters

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
    ├── FromMatch.kt                  # Non-group match sources (LINE/ALL_MATCHES)
    ├── IStructureSkips.kt            # skipHeaderLines/skipFooterLines carried by every companion
    ├── IStructure.kt                 # Base interface
    ├── IStructureCustomLine.kt       # Custom line interface
    └── IStructureMulti.kt            # Multi-structure interface

ksp-processor/                        # compile-time only
├── src/main/kotlin/aoc/ksp/
│   ├── StructureProcessor.kt         # SymbolProcessor, the visitor and the mode dispatch
│   ├── StructureOptions.kt           # @GenerateStructure arguments, read and validated once
│   ├── ParameterMappings.kt          # parameter -> expression, plus SUPPORTED_TYPES
│   └── CompanionTemplates.kt         # the three codegen templates
├── src/test/kotlin/aoc/ksp/          # kotlin-compile-testing: generated output and diagnostics
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
