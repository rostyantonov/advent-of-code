package aoc.common.util

import aoc.common.entity.IDataClass
import java.util.LinkedList

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

fun <E> List<E>.containsAny(otherList: List<E>): Boolean {
    forEach { if (otherList.contains(it)) return true }
    return false
}
