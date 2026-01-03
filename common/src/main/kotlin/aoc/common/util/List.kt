package aoc.common.util

import aoc.common.entity.IDataClass

fun <T : IDataClass<T>> List<T>.clone(): List<T> = cloneMutable()

@Suppress("UNCHECKED_CAST")
fun <T : IDataClass<T>> List<T>.cloneMutable(): MutableList<T> =
    mutableListOf<T>().also { list ->
        this.forEach { list.add(it.clone() as T) }
    }

fun <Type> List<Type>.splitBy(predicate: (Type) -> Boolean): List<List<Type>> =
    fold(mutableListOf(mutableListOf<Type>())) { acc, element ->
        if (predicate(element)) {
            acc.add(mutableListOf())
        } else {
            acc.last().add(element)
        }
        acc
    }
