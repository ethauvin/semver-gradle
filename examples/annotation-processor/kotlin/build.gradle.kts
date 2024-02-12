plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
    kotlin("jvm") version "1.7.22"
    kotlin("kapt") version "1.7.22"
}

// ./gradlew
// ./gradlew clean incrementPatch run
// ./gradlew clean incrementMinor run
// ./gradlew clean incrementMajor run

defaultTasks(ApplicationPlugin.TASK_RUN_NAME)

var semverProcessor = "net.thauvin.erik:semver:1.2.1"

dependencies {
    kapt(semverProcessor)
    implementation(semverProcessor)

    implementation(kotlin("stdlib"))
}

repositories {
    mavenLocal()
    mavenCentral()
}

application {
    mainClass.set("com.example.Main")
}

kapt {
    arguments {
        arg("semver.project.dir", projectDir)
    }
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
