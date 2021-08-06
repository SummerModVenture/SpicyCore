import java.time.*
import java.time.format.*
import net.minecraftforge.gradle.common.util.*
import org.jetbrains.kotlin.gradle.dsl.*

plugins {
    kotlin("jvm") version "1.5.21" apply false
    id("fabric-loom") version "0.9.+" apply false
    id("net.minecraftforge.gradle") version "5.1.+" apply false
    id("net.researchgate.release") version "2.8.1" apply false
}

ext["isRelease"] = !version.toString().endsWith("-SNAPSHOT")
val isRelease: Boolean by ext

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.minecraftforge.net")
    }
}
