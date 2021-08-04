import java.time.*
import java.time.format.*
import net.minecraftforge.gradle.common.util.*
import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("jvm") version "1.5.21"
    id("net.minecraftforge.gradle") version "5.1.+"
    id("net.researchgate.release") version "2.8.1"
    `maven-publish`
    signing
}

val modId: String by project
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
    named(apiSourceSet.implementationConfigurationName) {
        extendsFrom(implementation.get())
    }
    named(apiSourceSet.runtimeOnlyConfigurationName) {
        extendsFrom(runtimeOnly.get())
    }
    implementation {
        extendsFrom(library)
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(16))
configureKotlin {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        jvmTarget = "16"
    }
}

minecraft {
    val mappingsVersion: String by project
    mappings("official", mappingsVersion)

    runs {
        val runConfig: RunConfig.() -> Unit = {
            workingDirectory(project.file("run/$name"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")

            mods {
                register(modId) {
                    sources(sourceSets.main.get(), apiSourceSet)
                }
            }
        }

        val client by registering(runConfig)
        register("server", runConfig)

        client {
            val minecraftUUID: String? by project
            if (minecraftUUID != null)
                args("--uuid", minecraftUUID)
            val minecraftUsername: String? by project
            if (minecraftUsername != null)
                args("--username", minecraftUsername)
            val minecraftAccessToken: String? by project
            if (minecraftAccessToken != null)
                args("--accessToken", minecraftAccessToken)
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
}

dependencies {
    val mcVersion: String by project
    val forgeVersion: String by project
    minecraft("net.minecraftforge:forge:$mcVersion-$forgeVersion")

    library(kotlin("stdlib"))
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    val mcVersionRange: String by project
    val forgeVersionRange: String by project
    val loaderVersionRange: String by project
    val props = mapOf(
        "modName" to modName,
        "version" to project.version,
        "mcVersionRange" to mcVersionRange,
        "forgeVersionRange" to forgeVersionRange,
        "loaderVersionRange" to loaderVersionRange
    )
    inputs.properties(props)
    filesMatching("META-INF/mods.toml") {
        expand(props)
    }
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
            "Specification-Title"     to modId,
            "Specification-Vendor"    to "Forge",
            "Specification-Version"   to "1", // We are version 1 of ourselves
            "Implementation-Title"    to project.name,
            "Implementation-Version"  to project.version,
            "Implementation-Vendor"   to "spicymemes",
            "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}

fun configureKotlin(config: KotlinJvmCompile.() -> Unit) {
    tasks.named("compileApiKotlin", KotlinJvmCompile::class, config)
    tasks.compileKotlin(config)
    tasks.compileTestKotlin(config)
}
