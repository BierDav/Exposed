import dev.whyoleg.sweetspi.gradle.*

plugins {
    kotlin("jvm")
    alias(libs.plugins.dokka)
    alias(libs.plugins.ksp)
    alias(libs.plugins.whyoleg.sweetspi)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    withSweetSpi()
}

dependencies {
    api(project(":exposed-core"))

    implementation(project(":exposed-jdbc"))
}
