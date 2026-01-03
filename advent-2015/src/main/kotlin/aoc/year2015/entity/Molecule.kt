package aoc.year2015.entity

import aoc.common.entity.IStructureCustomLine

data class Molecule(
    val stringValue: String,
    val atoms: List<Atom>,
) {
    companion object : IStructureCustomLine<Molecule> {
        override fun create(
            line: String,
            collection: Sequence<MatchResult>,
        ): Molecule =
            Molecule(
                stringValue = line,
                atoms = collection.toList().map { Atom(it.value) },
            )
    }
}
