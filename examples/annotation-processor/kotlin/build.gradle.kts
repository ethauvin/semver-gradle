plugins {
    kotlin("jvm").version("1.3.0")
    application
    id("org.jetbrains.kotlin.kapt").version("1.3.0")
    id("net.thauvin.erik.gradle.semver").version("0.9.9-beta")
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

    implementation(kotlin("stdlib"))
}

repositories {
    mavenLocal()
    jcenter()
}

application {
    mainClassName = "com.example.Main"
}

semver {
//    properties = "example.properties"
//    keysPrefix = "example."
//    preReleaseKey = "release"
//    buildMetaKey = "meta"
}
