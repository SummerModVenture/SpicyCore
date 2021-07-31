import java.time.*
import java.time.format.*

plugins {
    kotlin("jvm") version "1.5.21"
    id("net.minecraftforge.gradle") version "5.1.+"
    id("net.researchgate.release") version "2.8.1"
    `maven-publish`
    signing
}

val modid: String by project
val modName: String by project
val archivesBaseName: String by project
val isRelease = !version.toString().endsWith("-SNAPSHOT")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(16))

minecraft {
    val mappingsVersion: String by project
    mappings("official", mappingsVersion)

    runs {
        create("client") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")

            mods {
                create("spicycore") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")

            mods {
                create("spicycore") {
                    source(sourceSets.main.get())
                }
            }
        }

        create("data") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")

            args("--mod", "examplemod", "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/"))

            mods {
                create("spicycore") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net")
    mavenLocal() // needed for local library-loading fix
}

dependencies {
    val mcVersion: String by project
    val forgeVersion: String by project
    minecraft("net.minecraftforge:forge:1.17.1-36.1.90-fix-1.17.x-library-loading")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "16"
    }

    compileTestKotlin {
        kotlinOptions.jvmTarget = "16"
    }

    jar {
        archiveBaseName.set(archivesBaseName)
        manifest()
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveBaseName.set(archivesBaseName)
    archiveClassifier.set("sources")
    from(project.sourceSets["main"].allSource)
}

val modJar by tasks.registering(Jar::class) {
    archiveBaseName.set(archivesBaseName)
    archiveClassifier.set("obf")
    from(sourceSets.main.get().output)
    manifest()
    finalizedBy("reobfJar")
}

tasks.assemble {
    dependsOn(modJar, sourcesJar)
}

publishing {
    publications {
        create<MavenPublication>("minecraft") {
            artifact(tasks.jar)
            artifact(modJar)
            artifact(sourcesJar)
            artifactId = archivesBaseName
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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["minecraft"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { isRelease }
}

release {
    preTagCommitMessage = "Release version"
    tagCommitMessage = "Release version"
    newVersionCommitMessage = "Next development version"
}

fun Jar.manifest() {
    manifest {
        attributes(
            "Specification-Title"     to modid,
            "Specification-Vendor"    to "Forge",
            "Specification-Version"   to "1", // We are version 1 of ourselves
            "Implementation-Title"    to project.name,
            "Implementation-Version"  to archiveVersion,
            "Implementation-Vendor"   to "spicymemes",
            "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}
