plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    ksp(project(":ksp-processor"))
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
