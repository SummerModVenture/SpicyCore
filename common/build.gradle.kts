import com.spicymemes.artifactory.tasks.GenerateKotlinModInfo

plugins {
    kotlin("jvm")
    id("fabric-loom")
    id("com.github.masterzach32.artifactory")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
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

val generateModInfo by tasks.registering(GenerateKotlinModInfo::class) {
    `package`.set("com.spicymemes.core.common")
}

signing {
    sign(publishing.publications["api"])
}
