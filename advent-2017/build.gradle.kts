plugins {
    id("aoc.kotlin-conventions")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":ksp-annotations"))
    implementation(project(":common"))
    ksp(project(":ksp-processor"))
    testImplementation(testFixtures(project(":common")))
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.suite)
}
