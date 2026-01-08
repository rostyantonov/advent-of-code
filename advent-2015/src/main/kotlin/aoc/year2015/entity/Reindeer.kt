package aoc.year2015.entity

import aoc.ksp.BaseEntity.getAsInt
import aoc.ksp.BaseEntity.getAsString
import aoc.ksp.GenerateStructure
import aoc.common.entity.IDataClass
import aoc.ksp.IStructure

@GenerateStructure
data class Reindeer(
    val name: String,
    val speed: Int,
    val moveTime: Int,
    val waitTime: Int,
) : IDataClass<Reindeer> {
    private var moving = true
    private var remainingTime = moveTime
    var distanceTraveled = 0
    var points = 0

    override fun clone(): Reindeer = copy()

    fun move() {
        if (moving) {
            distanceTraveled += speed
            if (--remainingTime == 0) {
                moving = false
                remainingTime = waitTime
            }
        } else {
            if (--remainingTime == 0) {
                moving = true
                remainingTime = moveTime
            }
        }
    }

    fun addPoint() {
        points++
    }
}
