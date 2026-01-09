package advent.of.code.common

/**
 * Annotation to mark Advent of Code solution classes for processing.
 * The KSP processor will generate utility code for these classes.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AdventSolution(
    val year: Int,
    val day: Int
)
