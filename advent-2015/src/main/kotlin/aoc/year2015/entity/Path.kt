package aoc.year2015.entity

data class Path(
    val points: List<String>,
    val distance: Int,
) {
    companion object {
        fun getAllPaths(
            values: List<String>,
            connections: Map<Pair<String, String>, Int>,
        ): List<Path> {
            // creating list of initial points
            var pointsPaths = values.map { Path(listOf(it), 0) }
            // doing while length less than points amount
            while (pointsPaths.first().points.size != values.size) {
                // new list to not update existing
                // (otherwise it will have only 1 path from each starting point)
                val newPointPaths = mutableListOf<Path>()
                pointsPaths.map { pointPath ->
                    // remove cities which already exists in path
                    values.filter { it !in pointPath.points }.map { nextPoint ->
                        // add new path with + 1 point
                        val newPath =
                            Path(
                                pointPath.points + nextPoint,
                                pointPath.distance + (connections[Pair(pointPath.points.last(), nextPoint)] ?: 0),
                            )
                        newPointPaths.add(newPath)
                    }
                }
                pointsPaths = newPointPaths
            }
            return pointsPaths
        }
    }
}
