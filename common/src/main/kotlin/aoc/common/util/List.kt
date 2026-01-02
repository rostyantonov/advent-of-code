package aoc.common.util

import aoc.common.entity.IDataClass

fun <T : IDataClass<T>> List<T>.clone(): List<T> = cloneMutable()

@Suppress("UNCHECKED_CAST")
fun <T : IDataClass<T>> List<T>.cloneMutable(): MutableList<T> =
    mutableListOf<T>().also { list ->
        this.forEach { list.add(it.clone() as T) }
    }
