plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    id("com.github.masterzach32.artifactory") version "0.3.1" apply false
    kotlin("plugin.serialization") version "1.5.21" apply false
    id("net.researchgate.release") version "2.8.1"
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

//val multiTargetJar by tasks.registering(Jar::class) {
//    val fabricProject = projects.fabric.dependencyProject
//    val forgeProject = projects.forge.dependencyProject
//    dependsOn(fabricProject.tasks.build, forgeProject.tasks.build)
//    val fabricJar = fabricProject.tasks["remapJar"]
//    val forgeJar = forgeProject.tasks["jar"]
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//    archiveBaseName.set(base.archivesName.get())
//    archiveVersion.set(fullVersion)
//    from(zipTree(fabricJar.outputs.files.singleFile))
//    from(zipTree(forgeJar.outputs.files.singleFile))
//}
//
//tasks.assemble {
//    dependsOn(multiTargetJar)
//}
//
//publishing {
//    publications {
//        register<MavenPublication>("mod") {
//            artifactId = base.archivesName.get()
//            version = fullVersion
//            artifact(multiTargetJar)
//        }
//    }
//}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}
