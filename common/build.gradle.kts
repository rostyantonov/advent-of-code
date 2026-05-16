plugins {
    id("aoc.kotlin-conventions")
    alias(libs.plugins.ksp)
    `java-test-fixtures`
}

dependencies {
    implementation(project(":ksp-annotations"))
    ksp(project(":ksp-processor"))
    implementation(libs.kotlin.reflect)
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.suite)

    // Test fixtures dependencies
    testFixturesApi(libs.kotlin.test)
    testFixturesApi(libs.junit.jupiter)
    testFixturesApi(libs.junit.suite)
    testFixturesApi(libs.kotlin.reflect)
}
