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

val apiSourceSet = sourceSets.create("api")
sourceSets {
    main {
        compileClasspath += apiSourceSet.output
        runtimeClasspath += apiSourceSet.output
        resources.srcDir("src/generated/resources")
    }
}

val library: Configuration by configurations.creating
configurations {
    get(apiSourceSet.implementationConfigurationName).extendsFrom(implementation.get())
    get(apiSourceSet.runtimeOnlyConfigurationName).extendsFrom(runtimeOnly.get())

    implementation {
        extendsFrom(library)
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(16))
configureKotlinJvmOptions(jvmTarget = "16")

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
                    sources(sourceSets.main.get(), apiSourceSet)
                }
            }
        }

        create("server") {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")

            mods {
                create("spicycore") {
                    sources(sourceSets.main.get(), apiSourceSet)
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
                    sources(sourceSets.main.get(), apiSourceSet)
                }
            }
        }
    }
}

// temporary fix for missing libs
minecraft.runs.all {
    lazyToken("minecraft_classpath") {
        library.copyRecursive().resolve().joinToString(File.pathSeparator) { it.absolutePath }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net")
//    mavenLocal() // needed for local library-loading fix
}

dependencies {
    val mcVersion: String by project
    val forgeVersion: String by project
    minecraft("net.minecraftforge:forge:$mcVersion-$forgeVersion")

    library(kotlin("stdlib"))
}


val updateModsToml by tasks.registering(Copy::class) {
    outputs.upToDateWhen { false }

    val mcVersionRange: String by project
    val forgeVersionRange: String by project
    val loaderVersionRange: String by project
    from(sourceSets.main.get().resources) {
        include("META-INF/mods.toml")
        expand(
            "modName" to modName,
            "version" to version,
            "mcVersionRange" to mcVersionRange,
            "forgeVersionRange" to forgeVersionRange,
            "loaderVersionRange" to loaderVersionRange,
        )
    }
    into("$buildDir/resources/main")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    exclude("META-INF/mods.toml")
    finalizedBy(updateModsToml)
}

tasks.classes {
    dependsOn(updateModsToml)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(archivesBaseName)
    from(sourceSets.main.get().output)
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfJar")
}

val apiJar by tasks.registering(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(archivesBaseName)
    archiveClassifier.set("api")
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfApiJar")

    // remove once fixed
    // Gradle should be able to pull them from the -sources jar.
    from(apiSourceSet.allSource)
}

val sourcesJar by tasks.registering(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(archivesBaseName)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    from(apiSourceSet.allSource)
}

val deobfJar by tasks.registering(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(archivesBaseName)
    archiveClassifier.set("deobf")
    from(sourceSets.main.get().output)
    from(apiSourceSet.output)
    manifest()
}

tasks.assemble {
    dependsOn(apiJar, sourcesJar, deobfJar)
}

reobf {
    create("apiJar") {
        classpath.from(sourceSets["api"].compileClasspath)
    }
    create("jar") {
        classpath.from(sourceSets.main.get().compileClasspath)
    }
}

publishing {
    publications {
        create<MavenPublication>("minecraft") {
            artifactId = archivesBaseName
            artifact(tasks.jar)
            artifact(apiJar)
            artifact(sourcesJar)
            artifact(deobfJar)
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
            "Implementation-Version"  to project.version,
            "Implementation-Vendor"   to "spicymemes",
            "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}

fun configureKotlinJvmOptions(jvmTarget: String) {
    tasks.compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }

    val compileApiKotlin by tasks.existing(org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile::class) {
        kotlinOptions.jvmTarget = jvmTarget
    }

    tasks.compileTestKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}
