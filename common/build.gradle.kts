import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    kotlin("plugin.serialization")
}

sourceSets {
    val api by creating
    main {
        compileClasspath += api.output
        runtimeClasspath += api.output
    }
    test {
        compileClasspath += api.output
        runtimeClasspath += api.output
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

tasks.jar {
    enabled = false
}

tasks.remapJar {
    enabled = false
}

tasks.remapSourcesJar {
    enabled = false
}
