import net.thauvin.erik.gradle.semver.SemverIncrementBuildMetaTask
import java.lang.String.format
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.42.0"
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
    kotlin("jvm") version "1.7.20"
}

// ./gradlew
// ./gradlew incrementPatch run
// ./gradlew incrementMinor run
// ./gradlew incrementMajor run
// ./gradlew incrementBuildMeta run

defaultTasks(ApplicationPlugin.TASK_RUN_NAME)

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("org.testng:testng:7.6.1")
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("com.example.MainKt")
//    project.afterEvaluate {
//        println("Version: $version")
//    }
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
