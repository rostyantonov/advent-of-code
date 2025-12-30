plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
rootProject.name = "advent-of-code"

include("main")
include("ksp-processor")
include("common")
include("advent-2015")
