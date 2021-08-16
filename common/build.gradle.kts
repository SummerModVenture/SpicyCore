import com.spicymemes.artifactory.tasks.*
import org.jetbrains.kotlin.gradle.tasks.*

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("com.github.masterzach32.artifactory")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

kotlin.sourceSets.main {
    kotlin.srcDir("src/main/generated")
}

dependencies {
    minecraft(libs.fabric.minecraft)
    mappings(loom.officialMojangMappings())

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

val generateModInfo by tasks.registering(GenerateModInfo::class) {
    location.set("com.spicymemes.core.common")
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

signing {
    sign(publishing.publications["api"])
}
