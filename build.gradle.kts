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
val mcVersion: String by project
val compositeVersion = "$mcVersion-$version"
val archivesBaseName: String by project
val apiArchivesBaseName = "$archivesBaseName-api"
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

val jarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(archivesBaseName)
    archiveVersion.set(compositeVersion)
}

tasks.jar(jarConfig)
tasks.jar {
    from(sourceSets.main.get().output)
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfJar")
}

val sourcesJar by tasks.registering(Jar::class, jarConfig)
sourcesJar {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    from(apiSourceSet.allSource)
}

val deobfJar by tasks.registering(Jar::class, jarConfig)
deobfJar {
    archiveClassifier.set("deobf")
    from(sourceSets.main.get().output)
    from(apiSourceSet.output)
    manifest()
}

val apiJarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(apiArchivesBaseName)
    archiveVersion.set(compositeVersion)
}

val apiJar by tasks.registering(Jar::class, apiJarConfig)
apiJar {
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfApiJar")
}

val apiSourcesJar by tasks.registering(Jar::class, apiJarConfig)
apiSourcesJar {
    archiveClassifier.set("sources")
    from(apiSourceSet.allSource)
}

val apiDeobfJar by tasks.registering(Jar::class, apiJarConfig)
apiDeobfJar {
    archiveClassifier.set("deobf")
    from(apiSourceSet.output)
    manifest()
}

tasks.assemble {
    dependsOn(sourcesJar, deobfJar, apiJar, apiSourcesJar, apiDeobfJar)
}

reobf {
    create("apiJar") {
        classpath.from(apiSourceSet.compileClasspath)
    }
    create("jar") {
        classpath.from(sourceSets.main.get().compileClasspath)
    }
}

publishing {
    publications {
        register<MavenPublication>("mod") {
            artifactId = archivesBaseName
            version = compositeVersion
            artifact(tasks.jar)
            artifact(sourcesJar)
            artifact(deobfJar)
        }

        register<MavenPublication>("api") {
            artifactId = apiArchivesBaseName
            version = compositeVersion
            artifact(apiJar)
            artifact(apiSourcesJar)
            artifact(apiDeobfJar)
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
    sign(publishing.publications["mod"], publishing.publications["api"])
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
