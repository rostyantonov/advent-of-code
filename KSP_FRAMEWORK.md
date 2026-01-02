# KSP Structure Generator Framework

This framework provides automatic code generation for entity classes that need to parse structured input using regex patterns.

## Overview

The `@GenerateStructure` annotation triggers KSP (Kotlin Symbol Processing) to automatically generate an `IStructure` implementation for your data classes. This eliminates boilerplate code for parsing regex-matched input.

## Features

- **Automatic Implementation Generation**: Annotate your data class and get a complete `IStructure` companion object
- **Type Support**: Supports Int, Long, String, Char, UShort (nullable and non-nullable)
- **Field Name Mapping**: Automatically maps regex named groups to field names
- **Extensible**: Easy to add support for additional types in `BaseEntity`

## Usage

### 1. Annotate Your Data Class

```kotlin
import aoc.common.entity.GenerateStructure

@GenerateStructure
data class Person(
    val name: String,
    val age: Int,
    val score: Long,
) {
    companion object  // Required for extension functions
}
```

### 2. Use the Generated Code

The framework generates a standalone `PersonCompanion` object and extension function:

```kotlin
// Define your regex with named groups matching the field names
val regex = Regex("(?<name>\\w+) age (?<age>\\d+) score (?<score>\\d+)")

// Use the generated companion to parse input
val person = Person.Companion.fromRegex("John age 25 score 100", regex)

// Or use the standalone object directly
val person2 = PersonCompanion.fromLine("Jane age 30 score 200", regex)
```

### 3. Integration with StructuredInput

```kotlin
class MyDay : AoCFileInput<List<Person>, Int>() {
    override val inputFunction
        get() = StructuredInput(
            regex = Regex("(?<name>\\w+) age (?<age>\\d+) score (?<score>\\d+)"),
            builder = PersonCompanion::fromLine,
        )::getStructInput
    
    override fun processPartOne(): Int {
        return input.sumOf { it.score.toInt() }
    }
}
```

## Supported Types

| Type | Non-Nullable | Nullable |
|------|--------------|----------|
| Int | ✅ | ✅ |
| Long | ✅ | ✅ |
| String | ✅ | ✅ |
| Char | ✅ | ✅ |
| UShort | ✅ | ✅ |
| Double | ✅ | ✅ |
| Float | ✅ | ✅ |
| Boolean | ✅ | ✅ |
| Byte | ✅ | ✅ |
| Short | ✅ | ✅ |
| Custom* | ✅ | ✅ |

\* Custom types require a `@FieldConverter` annotation with a `TypeConverter` implementation.

## Custom Type Converters

For complex types not supported by default, you can implement custom converters using the `TypeConverter` interface.

### 1. Create a Type Converter

```kotlin
import aoc.common.entity.TypeConverter
import aoc.common.entity.BaseEntity
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
import aoc.common.entity.GenerateStructure
import aoc.common.entity.FieldConverter

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
object PresentCompanion : IStructure<Present> {
    override fun create(collection: MatchGroupCollection): Present =
        Present(
            length = BaseEntity.getAsInt(collection, "length"),
            width = BaseEntity.getAsInt(collection, "width"),
            height = BaseEntity.getAsInt(collection, "height")
        )
}
```

## Adding New Type Support

To add support for a new type:

1. Add getter methods in `BaseEntity.kt`:
```kotlin
fun getAsYourType(collection: MatchGroupCollection, name: String) = ...
fun getAsNullableYourType(collection: MatchGroupCollection, name: String) = ...
```

2. Update `StructureProcessor.kt` to handle the new type:
```kotlin
typeString == "YourType" && !isNullable -> "BaseEntity.getAsYourType(collection, \"$name\")"
typeString == "YourType" && isNullable -> "BaseEntity.getAsNullableYourType(collection, \"$name\")"
```

## Limitations

- Classes with existing custom companion objects will be skipped (with a warning)
- Only primary constructor parameters are processed
- Regex named groups must match field names exactly

## Architecture

```
common/
├── src/main/kotlin/aoc/common/
│   ├── entity/
│   │   ├── GenerateStructure.kt     # Annotation definition
│   │   ├── BaseEntity.kt             # Type conversion helpers
│   │   └── IStructure.kt             # Base interface
│   └── ksp/
│       ├── StructureProcessor.kt     # KSP processor implementation
│       └── StructureProcessorProvider.kt  # Service provider
└── src/main/resources/META-INF/services/
    └── com.google.devtools.ksp.processing.SymbolProcessorProvider
```

## Build Configuration

The framework requires:
- KSP plugin applied to modules that use `@GenerateStructure`
- `ksp-api` dependency in the common module
- KSP processor dependency: `ksp(project(":common"))`

See `common/build.gradle.kts` and `advent-2015/build.gradle.kts` for configuration examples.
