plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.ksp.api)
}

kotlin {
    jvmToolchain(21)
}
