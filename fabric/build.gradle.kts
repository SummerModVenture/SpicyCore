
plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    signing
}

base {
    val archivesBaseName: String by project
    archivesName.set(archivesBaseName)
}

minecraft {}

sourceSets {
    main {
        val common = projects.common.dependencyProject
        common.sourceSets.forEach { sourceSet ->
            compileClasspath += sourceSet.output
            runtimeClasspath += sourceSet.output
        }
        resources.srcDir(common.sourceSets.main.get().resources.srcDirs)
    }
}

dependencies {
    val mcVersion: String by project
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())

    val fabricLoaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "16"
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") { expand(mapOf("version" to project.version)) }
}
