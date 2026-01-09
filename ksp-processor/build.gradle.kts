plugins {
    kotlin("jvm")
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.1.0-1.0.29")
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.squareup:kotlinpoet-ksp:2.0.0")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "17"
    }
}
