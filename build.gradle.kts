import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("com.github.ben-manes.versions") version "0.21.0"
    id("com.gradle.build-scan") version "2.3"
    id("com.gradle.plugin-publish") version "0.10.1"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14" // don't update until kotlin 1.3.41 is supported by Gradle
    id("org.jmailen.kotlinter") version "1.26.0" // don't update until kotlin 1.3.41 supported by Gradle
    id("org.sonarqube") version "2.7.1"
    kotlin("jvm") version "1.3.31"
}

version = "1.0.4"
group = "net.thauvin.erik.gradle"

object VersionInfo {
    const val kotlin = "1.3.31"
    const val spek = "2.0.5"
}
val versions: VersionInfo by extra { VersionInfo }

val github = "https://github.com/ethauvin/semver-gradle"
val packageName = "net.thauvin.erik.gradle.semver"

repositories {
    jcenter()
}

dependencies {
    implementation(gradleApi())

    testImplementation(kotlin("reflect", version = versions.kotlin))
    testImplementation(kotlin("test", version = versions.kotlin))
    testImplementation(gradleTestKit())

    testImplementation("org.spekframework.spek2:spek-dsl-jvm:${versions.spek}")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:${versions.spek}")
    implementation(kotlin("stdlib-jdk8", version = versions.kotlin))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        // Gradle 4.6
        kotlinOptions.apiVersion = "1.2"
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }

    withType<JacocoReport> {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
        }
    }

    "sonarqube" {
        dependsOn("jacocoTestReport")
    }
}

buildScan {
    link("GitHub", "https://github.com/ethauvin/semver-gradle/tree/master")
    if ("true" == System.getenv("CI")) {
        publishOnFailure()
        tag("CI")
    }
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
}

detekt {
    input = files("src/main/kotlin", "src/test/kotlin")
    filters = ".*/resources/.*,.*/build/.*"
    baseline = project.rootDir.resolve("detekt-baseline.xml")
}

kotlinter {
    ignoreFailures = false
    reporters = arrayOf("html")
    experimentalRules = false
    //disabledRules = arrayOf("import-ordering")
}

sonarqube {
    properties {
        property("sonar.projectName", "semver-gradle")
        property("sonar.projectKey", "ethauvin_semver-gradle")
        property("sonar.sourceEncoding", "UTF-8")
    }
}

gradlePlugin {
    plugins {
        create(project.name) {
            id = packageName
            displayName = "SemVer Plugin"
            description = "Semantic Version Plugin for Gradle"
            implementationClass = "$packageName.SemverPlugin"
        }
    }
}

pluginBundle {
    website = github
    vcsUrl = github
    tags = listOf("semver", "semantic", "version", "versioning", "auto-increment", "kotlin", "java")
    mavenCoordinates {
        groupId = project.group.toString()
        artifactId = project.name
    }
}
