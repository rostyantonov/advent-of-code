package aoc.year2017.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Instruction(
    val register: String,
    val operation: String,
    val amount: Int,
    val condRegister: String,
    val condOperator: String,
    val condAmount: Int,
)
