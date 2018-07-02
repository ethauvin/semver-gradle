import net.thauvin.erik.gradle.semver.SemverConfig

plugins {
    kotlin("jvm") version "1.2.50"
    application
    id("org.jetbrains.kotlin.kapt") version "1.2.50"
    id("net.thauvin.erik.gradle.semver") version "0.9.3-beta"
}

// ./gradlew
// ./gradlew clean incrementPatch run
// ./gradlew clean incrementMinor run
// ./gradlew clean incrementMajor run

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
