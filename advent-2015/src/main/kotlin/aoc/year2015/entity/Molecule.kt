package aoc.year2015.entity

import aoc.ksp.FromMatch
import aoc.ksp.GenerateStructure
import aoc.ksp.MatchPart

@GenerateStructure(customLine = true)
data class Molecule(
    @FromMatch(MatchPart.LINE) val stringValue: String,
    @FromMatch(MatchPart.ALL_MATCHES) val atoms: List<Atom>,
)
