@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.atomicfu)
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        optIn.add("kotlin.time.ExperimentalTime")
        optIn.add("kotlin.uuid.ExperimentalUuidApi")

        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    applyDefaultHierarchyTemplate()

    jvm()
//    iosArm64()
//    iosX64()
//    js {
//        browser()
//        nodejs()
//    }
//    wasmJs {
//        browser()
//        nodejs()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api(libs.kotlinx.coroutines)
                api(libs.kotlinx.datetime)
                api(libs.kotlinx.io.core)
                api(libs.stately.concurrent.collections)
                api(libs.kotlin.logging)
            }
        }
        val nonJvmMain by creating {
            dependsOn(commonMain)
        }
        val nonJsMain by creating {
            dependsOn(commonMain)
        }

        val jvmMain by getting {
            dependencies {
                api(kotlin("reflect"))
                api(libs.kotlinx.jvm.datetime)
                api(libs.slf4j)
            }
            dependsOn(nonJsMain)
        }

//        val androidMain by getting {
//            dependsOn(nonJvmMain)
//            dependsOn(nonJsMain)
//        }
//        val iosX64Main by getting {
//            dependsOn(nonJvmMain)
//            dependsOn(nonJsMain)
//        }
//        val iosArm64Main by getting {
//            dependsOn(nonJsMain)
//            dependsOn(nonJvmMain)
//        }
//        val jsMain by getting {
//            dependsOn(nonJvmMain)
//        }
//        val wasmJsMain by getting {
//            dependsOn(nonJvmMain)
//        }
    }
}
