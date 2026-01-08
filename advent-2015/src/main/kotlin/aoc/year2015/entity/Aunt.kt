package aoc.year2015.entity

import aoc.ksp.GenerateStructure

@GenerateStructure
data class Aunt(
    val index: Int,
    val param1name: String,
    val param1val: Int,
    val param2name: String,
    val param2val: Int,
    val param3name: String,
    val param3val: Int,
) {
    val param: Map<String, Int>
        get() =
            mapOf(
                param1name to param1val,
                param2name to param2val,
                param3name to param3val,
            )
}
