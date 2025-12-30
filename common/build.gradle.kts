plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    `java-test-fixtures`
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
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

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

ktlint {
    version.set("1.8.0")
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}
