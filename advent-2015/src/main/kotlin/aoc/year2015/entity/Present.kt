package aoc.year2015.entity

import aoc.common.entity.GenerateStructure

/**
 * Present entity representing a gift box with dimensions.
 * Uses @GenerateStructure to automatically generate PresentCompanion for parsing.
 */
@GenerateStructure
data class Present(
    val length: Int,
    val width: Int,
    val height: Int,
) {
    fun getAllSquares(): Int = 2 * length * width + 2 * width * height + 2 * height * length

    fun lowestSquare(): Int = minOf(length * width, width * height, height * length)

    fun lowestSidePerimeter(): Int = minOf((length + width) * 2, (length + height) * 2, (width + height) * 2)

    fun multiplySides() = width * height * length
}
