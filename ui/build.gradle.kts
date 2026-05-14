plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.factorydesigner.ui.resources"
    generateResClass = always
}

kotlin {
    jvm() // Define the jvm target here
    jvmToolchain(21)

    repositories{
        mavenCentral()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                // Move your dependencies here
                implementation(compose.desktop.currentOs)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.runtime)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

                implementation(project(":core"))
            }
        }
    }
}

