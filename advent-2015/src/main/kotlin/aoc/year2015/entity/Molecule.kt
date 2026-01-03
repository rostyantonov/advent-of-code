package aoc.year2015.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(customLine = true)
data class Molecule(
    val stringValue: String,
    val atoms: List<Atom>,
)
