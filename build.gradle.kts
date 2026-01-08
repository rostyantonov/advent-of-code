plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint.plugin) apply false
    alias(libs.plugins.detekt) apply false
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    description = "Deletes all build and generated directories"
    group = "build"
    
    // Delete root build directory
    delete(rootProject.layout.buildDirectory)
    
    // Delete build directories in all subprojects
    subprojects.forEach { subproject ->
        delete(subproject.layout.buildDirectory)
    }
    
    // Delete any generated directories (typically in build/generated)
    delete(fileTree(rootProject.projectDir) {
        include("**/build/generated/**")
    })
    
    // Also clean up standalone generated directories if any
    delete(fileTree(rootProject.projectDir) {
        include("**/generated")
    })
}
