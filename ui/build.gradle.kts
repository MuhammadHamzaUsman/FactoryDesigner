plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
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

                implementation(project(":core"))
            }
        }
    }
}

