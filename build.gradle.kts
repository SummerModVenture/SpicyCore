plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    id("com.github.masterzach32.artifactory") version "0.4-SNAPSHOT" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("net.researchgate.release") version "2.8.1"
    id("com.modrinth.minotaur") version "1.2.1" apply false
    base
    `maven-publish`
    signing
}

base.archivesName.set(findProperty("archivesBaseName").toString())
val archivesVersion: String by ext("${libs.versions.minecraft.get()}-$version")
val isRelease: Boolean by ext(!version.toString().endsWith("-SNAPSHOT"))

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
        maven("https://maven.masterzach32.net/artifactory/minecraft/")
    }

    publishing {
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

    signing {
        val signingKey: String? by project
        val signingPassword: String? by project
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    tasks.withType<Sign>().configureEach {
        onlyIf { isRelease }
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts", "-Xopt-in=kotlin.RequiresOptIn")
            jvmTarget = "16"
        }
    }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
