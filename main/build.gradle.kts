plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.suite)
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}