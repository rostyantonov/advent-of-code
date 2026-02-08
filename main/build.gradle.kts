plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(project(":advent-2015"))
    implementation(project(":advent-2016"))
    implementation(libs.gson)
    implementation(libs.kotlinx.coroutines.core)
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

ktlint {
    version.set(libs.versions.ktlint.get())
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

tasks.register("installGitHook") {
    doLast {
        val gitHooksDir = file(".git/hooks")
        if (!gitHooksDir.exists()) {
            gitHooksDir.mkdirs()
        }

        // Install pre-commit hook
        val preCommitHook = file(".git/hooks/pre-commit")
        preCommitHook.writeText(
            """
            #!/bin/sh
            echo "Running ktlint check before commit..."
            ./gradlew ktlintCheck --daemon
            if [ ${'$'}? -ne 0 ]; then
                echo "ktlint check failed. Please fix the issues before committing."
                exit 1
            fi
            """.trimIndent(),
        )
        preCommitHook.setExecutable(true)
        println("Git pre-commit hook installed successfully!")

        // Install pre-push hook
        val prePushHook = file(".git/hooks/pre-push")
        prePushHook.writeText(
            """
            #!/bin/sh
            echo "Running ktlint check before push..."
            ./gradlew ktlintCheck --daemon
            if [ ${'$'}? -ne 0 ]; then
                echo "ktlint check failed. Please fix the issues before pushing."
                exit 1
            fi
            """.trimIndent(),
        )

        prePushHook.setExecutable(true)
        println("Git pre-push hook installed successfully!")
    }
}

tasks.named("build") {
    dependsOn("installGitHook")
}
