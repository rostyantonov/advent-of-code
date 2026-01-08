package aoc.common.input

import java.io.InputStreamReader
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class FileInputDelegate : ReadWriteProperty<IAoCFileInput, List<String>> {
    private lateinit var value: List<String>

    override fun getValue(
        thisRef: IAoCFileInput,
        property: KProperty<*>,
    ): List<String> {
        if (!::value.isInitialized) {
            value = InputStreamReader(thisRef.inputFileUrl.openStream()).readLines()
        }
        return value
    }

    override fun setValue(
        thisRef: IAoCFileInput,
        property: KProperty<*>,
        value: List<String>,
    ) {
        this.value = value
    }
}
