import net.minecraftforge.gradle.common.util.*
import org.jetbrains.kotlin.gradle.tasks.*
import java.time.*
import java.time.format.*

plugins {
    kotlin("jvm")
    id("net.minecraftforge.gradle")
    id("com.github.masterzach32.artifactory")
    kotlin("plugin.serialization")
    id("com.modrinth.minotaur")
    `maven-publish`
    signing
}

val common = projects.common.dependencyProject
val archivesVersion: String by rootProject.ext
val isRelease: Boolean by rootProject.ext

minecraft {
    mappings("official", libs.versions.mappings.get())

    runs {
        val runConfig: RunConfig.() -> Unit = {
            sources = listOf(sourceSets.main.get()) + sourceSets["api"] + common.sourceSets["main"] + common.sourceSets["api"]
            workingDirectory(project.file("run/$name"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "DEBUG")
        }
        register("client") {
            runConfig()
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
        register("server", runConfig)
    }
}

forge {
    applyForgeMissingLibsTempfix()
    applyInvalidModuleNameFix()
}

repositories {
    maven("https://dvs1.progwml6.com/files/maven/")
    maven("https://modmaven.k-4u.nl")
}

dependencies {
    minecraft(libs.forge.minecraft)

    implementation(libs.kotlinx.serialization.core)

    obfRuntimeOnly("mezz.jei:jei-1.17.1:8.0.0.14")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        jvmTarget = "16"
    }
}

tasks.jar {
    manifest()
}

tasks.apiJar {
    manifest()
}

val publishModrinth by tasks.registering(com.modrinth.minotaur.TaskModrinthUpload::class) {
    dependsOn(tasks.build)
    val modrinthProjectId: String? by project
    val modrinthToken: String? by project
    onlyIf { isRelease && modrinthProjectId != null && modrinthToken != null }

    token = modrinthToken
    projectId = modrinthProjectId

    versionName = archivesVersion
    versionNumber = "${archivesVersion}-forge"
    uploadFile = tasks.jar.get()
    addGameVersion(libs.versions.minecraft.get())
    addLoader("forge")
}

tasks.publish {
    dependsOn(publishModrinth)
}

signing {
    sign(publishing.publications["mod"], publishing.publications["api"])
}

fun Jar.manifest() {
    manifest {
        attributes(
            "Specification-Title"      to "spicycore",
            "Specification-Vendor"     to "Forge",
            "Specification-Version"    to "1", // We are version 1 of ourselves
            "Implementation-Title"     to project.name,
            "Implementation-Version"   to project.version,
            "Implementation-Vendor"    to "spicymemes",
            "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}
