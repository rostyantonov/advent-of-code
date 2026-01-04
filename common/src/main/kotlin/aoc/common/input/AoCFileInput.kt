package aoc.common.input

import aoc.common.IAoCDay

abstract class AoCFileInput<InputFuncResult : Any, Result : Any> :
    IAoCDay<Result>,
    IAoCFileInput {
    override var rawInput: List<String> by FileInputDelegate()
    abstract val inputFunction: (List<String>) -> InputFuncResult
    protected var input: InputFuncResult by InputDelegate<InputFuncResult, Result>()
}
