package aoc.common.util
import aoc.common.entity.IDataClass
import java.util.SortedSet

@Suppress("UNCHECKED_CAST")
fun <T : IDataClass<T>> SortedSet<T>.cloneSortable(comparator: Comparator<T>): SortedSet<T> =
    sortedSetOf(comparator).also { result ->
        this.forEach { result.add(it.clone() as T) }
    }
