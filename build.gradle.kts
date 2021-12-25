import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import kotlin.collections.listOf

plugins {
    kotlin("jvm") version("1.6.0")
    id("com.github.johnrengelman.shadow") version "7.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
}

group = "run.dn5"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
}

bukkit {
    main = "$group.${rootProject.name}.Main"
    apiVersion = "1.17"
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("ddPn08")
    defaultPermission = BukkitPluginDescription.Permission.Default.OP
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
    }
    register<Copy>("devBuild"){
        dependsOn("clean", "shadowJar")
        from("$buildDir/libs/${rootProject.name}-$version.jar")
        into("$projectDir/.debug/plugins")
    }
}