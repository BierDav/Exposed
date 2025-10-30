plugins {
    kotlin("jvm")

    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    api(project(":exposed-core"))
}
