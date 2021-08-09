
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
    val fabricJar = projects.fabric.dependencyProject.tasks.named("remapJar")
    val forgeJar = projects.forge.dependencyProject.tasks.jar
    dependsOn(fabricJar, forgeJar)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set(base.archivesName.get())
    archiveVersion.set(compositeVersion)
    from(fabricJar.map { zipTree(it.outputs.files.singleFile) }.get())
    from(forgeJar.map { zipTree(it.outputs.files.singleFile) }.get())
}

publishing {
    publications {
        register<MavenPublication>("mod") {
            artifactId = base.archivesName.get()
            version = compositeVersion
            artifact(tasks.jar)
        }
    }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
