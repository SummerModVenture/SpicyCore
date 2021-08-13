
plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    id("com.github.masterzach32.artifactory") version "0.2.2" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("net.researchgate.release") version "2.8.1"
    `java-library`
    `maven-publish`
}

base.archivesName.set(findProperty("archivesBaseName").toString())
ext["fullVersion"] = "${libs.versions.minecraft.get()}-$version"
val fullVersion: String by ext
ext["isRelease"] = !version.toString().endsWith("-SNAPSHOT")
val isRelease: Boolean by ext

allprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    repositories {
        mavenCentral()
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
}

tasks.jar {
    val fabricJar = projects.fabric.dependencyProject.tasks["remapJar"]
    val forgeJar = projects.forge.dependencyProject.tasks.jar.get()
    dependsOn(fabricJar, forgeJar)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set(base.archivesName.get())
    archiveVersion.set(fullVersion)
    from(zipTree(fabricJar.outputs.files.singleFile))
    from(zipTree(forgeJar.outputs.files.singleFile))
}

publishing {
    publications {
        register<MavenPublication>("mod") {
            artifactId = base.archivesName.get()
            version = fullVersion
            artifact(tasks.jar)
        }
    }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
