import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

base.archivesName.set("${findProperty("archivesBaseName")}-api")
val compositeVersion: String by rootProject.ext
val isRelease: Boolean by rootProject.ext

val apiSourceSet = sourceSets.create("api")
sourceSets {
    main {
        compileClasspath += apiSourceSet.output
        runtimeClasspath += apiSourceSet.output
    }
    test {
        compileClasspath += apiSourceSet.output
        runtimeClasspath += apiSourceSet.output
    }
}

configurations {
    get("apiImplementation").extendsFrom(compileClasspath.get())
}

kotlin.sourceSets.main {
    kotlin.srcDir("src/main/generated")
}

minecraft {}

dependencies {
    minecraft(libs.fabric.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.fabric.loader)

    implementation(libs.kotlinx.serialization.core)

    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

val generateModInfo by tasks.registering {
    description = "Generates the ModInfo.kt source file."
    val modId: String by rootProject
    val modName: String by rootProject
    doLast {
        mkdir("src/main/generated/com/spicymemes/core/common")
        file("src/main/generated/com/spicymemes/core/common/ModInfo.kt").writeText("""
            package com.spicymemes.core.common
            
            const val MOD_ID = "$modId"
            const val MOD_NAME = "$modName"
            const val MOD_VERSION = "$version"
        """.trimIndent() + "\n")
    }
}

tasks.compileKotlin {
    dependsOn(generateModInfo)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts", "-Xopt-in=kotlin.RequiresOptIn")
        jvmTarget = "16"
    }
}

val apiJarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveVersion.set(compositeVersion)
}
val apiJar by tasks.registering(Jar::class) {
    apiJarConfig()
    dependsOn(tasks.remapJar)
    from(apiSourceSet.output)
}
val sourcesJar by tasks.registering(Jar::class) {
    apiJarConfig()
    archiveClassifier.set("sources")
    from(apiSourceSet.allSource)
}
tasks.assemble {
    dependsOn(apiJar, sourcesJar)
}

publishing {
    publications {
        register<MavenPublication>("api") {
            artifactId = base.archivesName.get()
            version = compositeVersion
            artifact(apiJar) {
                builtBy(tasks.remapJar)
            }
            artifact(sourcesJar) {
                builtBy(tasks.remapSourcesJar)
            }
        }
    }
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications["api"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { isRelease }
}
