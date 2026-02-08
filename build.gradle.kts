plugins {
    kotlin("jvm") version "2.2.0" apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}