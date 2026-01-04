package aoc.common.entity

import kotlin.reflect.KClass

/**
 * Annotation to specify a custom type converter for a field in a @GenerateStructure class.
 *
 * Use this when you need to convert complex types that aren't supported by default.
 *
 * Example:
 * ```
 * @GenerateStructure
 * data class Instruction(
 *     @FieldConverter(PositionConverter::class)
 *     val start: Position,
 *     @FieldConverter(PositionConverter::class)
 *     val end: Position,
 *     val action: String
 * )
 * ```
 *
 * The converter class must implement TypeConverter<T> where T is the field type.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class FieldConverter(
    /**
     * The KClass of the TypeConverter implementation to use for this field.
     * Must implement TypeConverter<T> where T is the type of the annotated field.
     */
    val converter: KClass<out TypeConverter<*>>,
)
