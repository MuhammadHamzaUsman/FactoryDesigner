plugins {
    application
    id("java")
    kotlin("jvm") version "2.0.0" apply false
    id("org.jetbrains.compose") version "1.6.10" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

subprojects {
    plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper> {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
            jvmToolchain(21)
        }
    }
}

application {
    mainClass.set("org.example.Main")
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":ui"))
}