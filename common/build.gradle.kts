
plugins {
    kotlin("jvm")
    id("fabric-loom")
}

sourceSets {
    val api by creating
    main {
        compileClasspath += api.output
        runtimeClasspath += api.output
    }
}

configurations {
    val compileClasspath by getting
    get("apiImplementation").extendsFrom(compileClasspath)
}

loom {

}

dependencies {
    val mcVersion: String by project
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.officialMojangMappings())

    val fabricLoaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    val fabricVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
}

tasks.remapJar {
    enabled = false
}

tasks.remapSourcesJar {
    enabled = false
}
