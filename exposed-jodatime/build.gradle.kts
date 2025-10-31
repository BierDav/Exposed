plugins {
    kotlin("jvm")
    alias(libs.plugins.serialization)

    alias(libs.plugins.dokka)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
    }
}

dependencies {
    api(project(":exposed-core"))
    testCompileOnly(project(":exposed-jdbc"))
    api(libs.joda.time)
    testImplementation(project(":exposed-dao"))
    testImplementation(project(":exposed-tests"))
    testImplementation(project(":exposed-json"))
    testImplementation(libs.junit)
    testImplementation(kotlin("test-junit"))
}
