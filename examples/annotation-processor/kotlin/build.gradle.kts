import net.thauvin.erik.gradle.semver.SemverConfig

buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("net.thauvin.erik.gradle:semver:0.9.2-beta")
    }
}

plugins {
    kotlin("jvm") version "1.2.50"
    application
    id("org.jetbrains.kotlin.kapt") version "1.2.50"
}

apply {
    plugin("net.thauvin.erik.gradle.semver")
}


// ./gradlew
// ./gradlew runJava
// ./gradlew run runJava

defaultTasks(ApplicationPlugin.TASK_RUN_NAME)

var semverProcessor = "net.thauvin.erik:semver:1.1.0-beta"

dependencies {
    kapt(semverProcessor)
    compileOnly(semverProcessor)

    compile(kotlin("stdlib"))
}

repositories {
    mavenLocal()
    jcenter()
}

application {
    mainClassName = "com.example.Main"
}

configure<SemverConfig> {
    //properties = "example.properties"
    //keysPrefix = "example."
    //preReleaseKey = "release"
    //buildMetaKey = "meta"
}
