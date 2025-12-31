package aoc.year2015.entity

import aoc.common.entity.Position
import aoc.common.util.safeValue
import aoc.ksp.FieldConverter
import aoc.ksp.GenerateStructure

@GenerateStructure
data class Instruction(
    val cmd: String,
    @param:FieldConverter(PositionConverter::class)
    val start: Position,
    @param:FieldConverter(PositionConverter::class)
    val end: Position,
) {
    val command: Command = safeValue(cmd)
    val rowRange = start.row..end.row
    val colRange = start.col..end.col
}
