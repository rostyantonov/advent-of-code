package aoc.year2015.entity

import aoc.common.entity.FieldConverter
import aoc.common.entity.GenerateStructure
import aoc.common.entity.Position
import aoc.common.util.safeValue

@GenerateStructure
data class Instruction(
    val cmd: String,
    @FieldConverter(PositionConverter::class)
    val start: Position,
    @FieldConverter(PositionConverter::class)
    val end: Position,
) {
    val command: Command = safeValue(cmd)
    val rowRange = start.row..end.row
    val colRange = start.col..end.col
}
