package aoc.common.util

inline fun <reified T : Enum<T>> safeValue(type: String): T = java.lang.Enum.valueOf(T::class.java, type.replace(' ', '_').uppercase())

inline fun <reified T : Enum<T>> valueOrElse(
    type: String?,
    value: T,
): T {
    if (type.isNullOrEmpty()) {
        return value
    }
    return java.lang.Enum.valueOf(T::class.java, type.replace(' ', '_').uppercase())
}
