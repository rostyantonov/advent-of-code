package aoc.common.util

import aoc.ksp.BaseEntity

/**
 * Resolves [type] to an enum constant, falling back to [value] when nothing matches.
 *
 * Matching is `BaseEntity`'s, the same rule the processor emits for every enum parameter, so an
 * `@StructureName` alias is honoured here too. All this adds is the default: the converters that
 * call it want an absent or unrecognised group to mean something rather than to fail.
 */
inline fun <reified T : Enum<T>> valueOrElse(
    type: String?,
    value: T,
): T {
    if (type.isNullOrEmpty()) {
        return value
    }
    return BaseEntity.asNullableEnum<T>(type) ?: value
}
