plugins {
    id("application")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("net.thauvin.erik.gradle.semver") version "1.0.4"
    kotlin("jvm") version "2.2.0"
    kotlin("kapt") version "1.9.25"
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

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("com.example.Main")
}

kapt {
    arguments {
        arg("semver.project.dir", projectDir.absolutePath)
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
