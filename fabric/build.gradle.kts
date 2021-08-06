import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
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
        common.sourceSets.forEach { sourceSet ->
            compileClasspath += sourceSet.output
            runtimeClasspath += sourceSet.output
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
        extendsFrom(implementation.get())
    }
    named(apiSourceSet.runtimeOnlyConfigurationName) {
        extendsFrom(runtimeOnly.get())
    }
}

dependencies {
    minecraft(libs.fabric.minecraft)
    mappings(loom.officialMojangMappings())

    modImplementation(libs.bundles.fabric.implmentation)
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
}

val jarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveVersion.set(compositeVersion)
}

tasks.jar {
    jarConfig()
    common.sourceSets.forEach {
        from(it.output.classesDirs)
    }
    from(apiSourceSet.output)
}

val sourcesJar by tasks.registering(Jar::class) {
    jarConfig()
    archiveClassifier.set("sources")
    common.sourceSets.forEach {
        from(it.allSource)
    }
    from(apiSourceSet.allSource)
    from(sourceSets.main.get().allSource)
}

val apiJarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
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

publishing {
    publications {
        register<MavenPublication>("mod") {
            version = compositeVersion
            artifact(tasks.jar)
            artifact(sourcesJar)
            //artifact(deobfJar)
        }

        register<MavenPublication>("api") {
            artifactId = apiArchivesBaseName
            version = compositeVersion
            artifact(apiJar)
            artifact(apiSourcesJar)
            //artifact(apiDeobfJar)
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
