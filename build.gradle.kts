plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktlint) apply false
}

group = "advent.of.code"
version = "1.0-SNAPSHOT"

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

subprojects {
    tasks.register("setupHooks") {
        dependsOn(rootProject.tasks.named("installGitHook"))
    }

    tasks.matching { it.name == "build" }.configureEach {
        dependsOn("setupHooks")
    }
}


