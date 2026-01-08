plugins {
    id("aoc.kotlin-conventions")
}

dependencies {
    implementation(project(":ksp-annotations"))
    implementation(libs.ksp.api)
}
