import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

base {
    val archivesBaseName: String by project
    archivesName.set("$archivesBaseName-fabric")
}
val apiArchivesBaseName = base.archivesName.get() + "-api"
val common = projects.common.dependencyProject
val compositeVersion: String by rootProject.ext
val isRelease: Boolean by rootProject.ext

minecraft {}

val apiSourceSet = sourceSets.create("api")
sourceSets {
    apiSourceSet.apply {
        common.sourceSets["api"].also { commonApi ->
            compileClasspath += commonApi.output
            runtimeClasspath += commonApi.output
        }
    }
    main {
        common.sourceSets.forEach { sourceSet ->
            compileClasspath += sourceSet.output
            runtimeClasspath += sourceSet.output
        }
        compileClasspath += apiSourceSet.output
        runtimeClasspath += apiSourceSet.output
        //resources.srcDir(common.sourceSets.main.get().resources.srcDirs)
    }
}

configurations {
    named(apiSourceSet.implementationConfigurationName) {
        extendsFrom(compileClasspath.get())
    }
}

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

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    inputs.property("version", project.version)
    filesMatching("fabric.mod.json") { expand("version" to project.version) }
    from(common.sourceSets.main.get().resources)
}

val jarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveVersion.set(compositeVersion)
}
tasks.jar {
    jarConfig()
    from(common.sourceSets["api"].output)
    from(common.sourceSets.main.get().output)
    from(apiSourceSet.output)
}
val sourcesJar by tasks.registering(Jar::class) {
    jarConfig()
    archiveClassifier.set("sources")
    from(common.sourceSets["api"].allSource)
    from(common.sourceSets.main.get().allSource)
    from(apiSourceSet.allSource)
    from(sourceSets.main.get().allSource)
}

val apiJarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveBaseName.set(apiArchivesBaseName)
    archiveVersion.set(compositeVersion)
}
val apiJar by tasks.registering(Jar::class) {
    apiJarConfig()
    dependsOn(tasks.remapJar)
    from(common.sourceSets["api"].output)
    from(apiSourceSet.output)
}
val apiSourcesJar by tasks.registering(Jar::class) {
    apiJarConfig()
    dependsOn(tasks.remapSourcesJar)
    archiveClassifier.set("sources")
    from(common.sourceSets["api"].allSource)
    from(apiSourceSet.allSource)
}

tasks.assemble {
    dependsOn(sourcesJar, apiJar, apiSourcesJar)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("mod") {
                artifactId = base.archivesName.get()
                version = compositeVersion
                artifact(tasks.jar.get().archiveFile) {
                    builtBy(tasks.remapJar)
                }
                artifact(sourcesJar) {
                    builtBy(tasks.remapSourcesJar)
                }
            }

            register<MavenPublication>("api") {
                artifactId = apiArchivesBaseName
                version = compositeVersion
                artifact(apiJar) {
                    builtBy(tasks.remapJar)
                }
                artifact(apiSourcesJar) {
                    builtBy(tasks.remapSourcesJar)
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
}

tasks.withType<Sign>().configureEach {
    onlyIf { isRelease }
}
