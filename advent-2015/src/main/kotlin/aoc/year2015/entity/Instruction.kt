package aoc.year2015.entity

import aoc.common.entity.Position
import aoc.common.entity.PositionConverter
import aoc.ksp.FieldConverter
import aoc.ksp.GenerateStructure

@GenerateStructure
data class Instruction(
    val cmd: Command,
    @param:FieldConverter(PositionConverter::class)
    val start: Position,
    @param:FieldConverter(PositionConverter::class)
    val end: Position,
) {
    val rowRange = start.row..end.row
    val colRange = start.col..end.col
}
