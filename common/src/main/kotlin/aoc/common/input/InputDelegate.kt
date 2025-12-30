package aoc.common.input

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class InputDelegate<InputFuncResult : Any, Result : Any> : ReadWriteProperty<AoCFileInput<InputFuncResult, Result>, InputFuncResult> {
    private lateinit var value: InputFuncResult

    override fun getValue(
        thisRef: AoCFileInput<InputFuncResult, Result>,
        property: KProperty<*>,
    ): InputFuncResult {
        if (!::value.isInitialized) {
            value = thisRef.inputFunction(thisRef.rawInput)
        }
        return value
    }

    override fun setValue(
        thisRef: AoCFileInput<InputFuncResult, Result>,
        property: KProperty<*>,
        value: InputFuncResult,
    ) {
        this.value = value
    }
}
