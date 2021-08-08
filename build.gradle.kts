
plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("net.researchgate.release") version "2.8.1"
    `java-library`
    `maven-publish`
}

base.archivesName.set(findProperty("archivesBaseName").toString())
ext["compositeVersion"] = "${libs.versions.minecraft.get()}-$version"
val compositeVersion: String by ext
ext["isRelease"] = !version.toString().endsWith("-SNAPSHOT")
val isRelease: Boolean by ext

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.minecraftforge.net")
    }
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveVersion.set(compositeVersion)
    from(
        subprojects
            .flatMap { it.sourceSets }
            .filter { it.name != "test" }
            .map { it.output }
    )
}

publishing {
    publications {
        register<MavenPublication>("mod") {
            version = compositeVersion
            artifact(tasks.jar)
        }
    }

    repositories {
        val mavenUsername: String? by project
        val mavenPassword: String? by project
        if (mavenUsername != null && mavenPassword != null) {
            maven {
                if (isRelease) {
                    name = "Releases"
                    url = uri("https://maven.masterzach32.net/artifactory/minecraft-releases/")
                } else {
                    name = "Snapshots"
                    url = uri("https://maven.masterzach32.net/artifactory/minecraft-snapshots/")
                }
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
