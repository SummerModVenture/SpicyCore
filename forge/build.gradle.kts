import net.minecraftforge.gradle.common.util.*
import org.jetbrains.kotlin.gradle.tasks.*
import java.time.*
import java.time.format.*

plugins {
    kotlin("jvm")
    id("net.minecraftforge.gradle")
    `maven-publish`
    signing
}

base {
    val archivesBaseName: String by project
    archivesName.set("$archivesBaseName-forge")
}
val apiArchivesBaseName = base.archivesName.get() + "-api"
val common = projects.common.dependencyProject
val compositeVersion: String by rootProject.ext
val isRelease: Boolean by rootProject.ext

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

minecraft {
    mappings("official", libs.versions.mappings.get())

    runs {
        val runConfig: RunConfig.() -> Unit = {
            sources = listOf(sourceSets.main.get()) + common.sourceSets
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

// temporary fix for missing libs
minecraft.runs.all {
    lazyToken("minecraft_classpath") {
        library.copyRecursive().resolve().joinToString(File.pathSeparator) { it.absolutePath }
    }
}

dependencies {
    minecraft(libs.forge.minecraft)

    library(kotlin("stdlib"))
}

//java.toolchain.languageVersion.set(JavaLanguageVersion.of(16))
tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.contracts.ExperimentalContracts")
        jvmTarget = "16"
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/mods.toml") { expand("version" to project.version) }
}

val jarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveVersion.set(compositeVersion)
}

tasks.jar {
    jarConfig()
    common.sourceSets.forEach {
        from(it.output)
    }
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfJar")
}

val sourcesJar by tasks.registering(Jar::class) {
    jarConfig()
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    common.sourceSets.forEach {
        from(it.allSource)
    }
    from(apiSourceSet.allSource)
}

val deobfJar by tasks.registering(Jar::class) {
    jarConfig()
    archiveClassifier.set("deobf")
    from(sourceSets.main.get().output)
    common.sourceSets.forEach {
        from(it.output)
    }
    from(apiSourceSet.output)
    manifest()
}

val apiJarConfig: Jar.() -> Unit = {
    duplicatesStrategy = DuplicatesStrategy.FAIL
    archiveBaseName.set(apiArchivesBaseName)
    archiveVersion.set(compositeVersion)
}

val apiJar by tasks.registering(Jar::class) {
    apiJarConfig()
    from(common.sourceSets["api"].output)
    from(apiSourceSet.output)
    manifest()
    finalizedBy("reobfApiJar")
}

val apiSourcesJar by tasks.registering(Jar::class) {
    apiJarConfig()
    archiveClassifier.set("sources")
    from(common.sourceSets["api"].allSource)
    from(apiSourceSet.allSource)
}

val apiDeobfJar by tasks.registering(Jar::class) {
    apiJarConfig()
    archiveClassifier.set("deobf")
    from(common.sourceSets["api"].output)
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

fun Jar.manifest() {
    manifest {
        attributes(
            "Specification-Title"     to "spicycore",
            "Specification-Vendor"    to "Forge",
            "Specification-Version"   to "1", // We are version 1 of ourselves
            "Implementation-Title"    to project.name,
            "Implementation-Version"  to project.version,
            "Implementation-Vendor"   to "spicymemes",
            "Implementation-Timestamp" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}
