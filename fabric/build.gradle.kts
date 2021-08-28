import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("com.github.masterzach32.artifactory")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

val archivesVersion: String by rootProject.ext
val isRelease: Boolean by rootProject.ext

repositories {
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft(libs.fabric.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.bundles.fabric.implmentation)
    modRuntime(libs.fabric.modmenu)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        jvmTarget = "16"
    }
}

val publishModrinth by tasks.registering(com.modrinth.minotaur.TaskModrinthUpload::class) {
    dependsOn(tasks.build)
    val modrinthProjectId: String? by project
    val modrinthToken: String? by project
    onlyIf { isRelease && modrinthProjectId != null && modrinthToken != null }

    token = modrinthToken
    projectId = modrinthProjectId

    versionName = archivesVersion
    versionNumber = "${archivesVersion}-fabric"
    uploadFile = tasks.remapJar.get()
    addGameVersion(libs.versions.minecraft.get())
    addLoader("fabric")
}

tasks.publish {
    dependsOn(publishModrinth)
}

signing {
    sign(publishing.publications["mod"], publishing.publications["api"])
}
