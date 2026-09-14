plugins {
    id("aoc.kotlin-conventions")
}

// kotlin-compile-testing drives the compiler through APIs Kotlin marks experimental; the opt-in is
// scoped to the tests so nothing in the processor itself can reach for them by accident.
tasks.named<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>("compileTestKotlin") {
    compilerOptions {
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
    }
}

dependencies {
    implementation(project(":ksp-annotations"))
    implementation(libs.ksp.api)

    testImplementation(libs.kctfork.ksp)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.junit.jupiter)
}
