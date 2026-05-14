plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint.plugin)
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
    android.set(false)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

tasks.register("installGitHook") {
    val hooksDir = layout.projectDirectory.dir("../.git/hooks")
    doLast {
        val gitHooksDir = hooksDir.asFile
        if (!gitHooksDir.exists()) {
            gitHooksDir.mkdirs()
        }

        val preCommitHook = gitHooksDir.resolve("pre-commit")
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

        val prePushHook = gitHooksDir.resolve("pre-push")
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
