plugins {
    kotlin("jvm") version "1.3.30"
    application
    id("org.jetbrains.kotlin.kapt") version "1.3.30"
    id("net.thauvin.erik.gradle.semver") version "0.9.9-beta"
    id("com.github.ben-manes.versions") version "0.21.0"
}

// ./gradlew
// ./gradlew clean incrementPatch run
// ./gradlew clean incrementMinor run
// ./gradlew clean incrementMajor run

defaultTasks(ApplicationPlugin.TASK_RUN_NAME)

var semverProcessor = "net.thauvin.erik:semver:1.1.1"

dependencies {
    kapt(semverProcessor)
    compileOnly(semverProcessor)

    implementation(kotlin("stdlib"))
}

repositories {
    mavenLocal()
    jcenter()
}

application {
    mainClassName = "com.example.Main"
}

tasks {
    "run"(JavaExec::class) {
        doFirst {
            println("Verion: $version")
        }

//        args = listOf("example.properties")
        args = listOf("version.properties")
    }
}

semver { 
//    properties = "example.properties"
//    keysPrefix = "example."
//    preReleaseKey = "release"
//    buildMetaKey = "meta"
}
