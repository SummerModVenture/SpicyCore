import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("com.github.masterzach32.artifactory")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

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

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["mod"], publishing.publications["api"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { isRelease }
}
