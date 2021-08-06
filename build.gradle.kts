
plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    id("net.researchgate.release") version "2.8.1"
    `java-library`
}

ext["compositeVersion"] = "${libs.versions.minecraft.get()}-$version"
ext["isRelease"] = !version.toString().endsWith("-SNAPSHOT")
val isRelease: Boolean by rootProject.ext

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.minecraftforge.net")
    }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
