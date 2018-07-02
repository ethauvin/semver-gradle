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
}

apply {
    plugin("net.thauvin.erik.gradle.semver")
}

// ./gradlew
// ./gradlew incrementPatch run
// ./gradlew incrementMinor run
// ./gradlew incrementMajor run

defaultTasks(ApplicationPlugin.TASK_RUN_NAME)

dependencies {
    compile(kotlin("stdlib"))
    testCompile("org.testng:testng:6.14.3")
}

repositories {
    jcenter()
}

application {
    mainClassName = "com.example.MainKt"
}

configure<SemverConfig> {
    //properties = "example.properties"
    //keysPrefix = "example."
    //preReleaseKey = "release"
    //buildMetaKey = "meta"
}

tasks {
    withType<Test> {
        useTestNG()
    }

    val run by getting {
        doFirst {
            println("Version: $version")
        }
    }
}
