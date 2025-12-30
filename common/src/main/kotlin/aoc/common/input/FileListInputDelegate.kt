package aoc.common.input

import java.io.InputStreamReader
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FileListInputDelegate(
    val testFilesAmount: Int,
) : ReadWriteProperty<IAoCFileInput, List<List<String>>> {
    private lateinit var value: List<List<String>>

    override fun getValue(
        thisRef: IAoCFileInput,
        property: KProperty<*>,
    ): List<List<String>> {
        if (!::value.isInitialized) {
            value =
                buildList {
                    repeat(testFilesAmount) { index ->
                        add(InputStreamReader(thisRef.inputFileIndexUrl(index).openStream()).readLines())
                    }
                }
        }
        return value
    }

    override fun setValue(
        thisRef: IAoCFileInput,
        property: KProperty<*>,
        value: List<List<String>>,
    ) {
        this.value = value
    }
}
