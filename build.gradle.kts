plugins {
    kotlin("jvm") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }
}