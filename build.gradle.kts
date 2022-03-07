import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10-RC"
    java
    id("application")
}

group = "olivermakesco.de"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "17"
        }
    }
    jar {
        manifest {
            attributes["Main-Class"] = "ktdoom.MainKt"
        }
        configurations["compileClasspath"].forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }
}