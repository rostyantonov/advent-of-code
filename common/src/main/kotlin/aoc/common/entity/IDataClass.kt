package aoc.common.entity

interface IDataClass<DataType> {
    fun clone(): IDataClass<DataType>
}
