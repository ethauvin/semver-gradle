import net.thauvin.erik.gradle.semver.SemverConfig

plugins {
    kotlin("jvm") version "1.2.50"
    application
    id("net.thauvin.erik.gradle.semver") version "0.9.4-beta"
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

    val run by getting(JavaExec::class) {
        doFirst {
            println("Version: $version")
        }

        // args = listof("example.properties")
        args = listOf("version.properties")
    }
}
