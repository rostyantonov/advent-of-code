package aoc.ksp

interface IStructureMulti<Type : Any> {
    fun fromLine(
        line: String,
        regexArray: Array<Regex>,
    ): Type {
        lateinit var typedObject: Type
        regexArray.forEach { regex ->
            if (!regex.matchEntire(line)?.groups.isNullOrEmpty()) {
                typedObject = create(regex.matchEntire(line)!!.groups)
                return@forEach
            }
        }
        return typedObject
    }

    fun create(collection: MatchGroupCollection): Type
}
