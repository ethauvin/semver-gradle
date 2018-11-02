import net.thauvin.erik.gradle.semver.SemverIncrementBuildMetaTask
import java.lang.String.format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    kotlin("jvm") version "1.2.50"
    application
    id("net.thauvin.erik.gradle.semver") version "0.9.8-beta"
}

// ./gradlew
// ./gradlew incrementPatch run
// ./gradlew incrementMinor run
// ./gradlew incrementMajor run
// ./gradlew incrementBuildMeta run

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

semver {
//    properties = "example.properties"
//    keysPrefix = "example."
//    preReleaseKey = "release"
//    buildMetaKey = "meta"
}

tasks {
    withType<Test> {
        useTestNG()
    }

    "incrementBuildMeta"(SemverIncrementBuildMetaTask::class) {
        doFirst {
//            buildMeta = format("%03d", buildMeta.toInt() + 1)
            buildMeta = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
        }
    }

    "run"(JavaExec::class) {
        doFirst {
            println("Version: $version")
        }

//        args = listOf("example.properties")
        args = listOf("version.properties")
    }
}
