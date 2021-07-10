import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.gradle.plugin-publish") version "0.15.0"
    id("io.gitlab.arturbosch.detekt") version "1.18.0-RC1"
    id("jacoco")
    id("java")
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.gradle.kotlin.kotlin-dsl") version "2.1.4"
    id("org.sonarqube") version "3.3"
    kotlin("jvm") version "1.4.31" // Don't upgrade until kotlin-dsl plugin is upgraded.
}

version = "1.0.5"
group = "net.thauvin.erik.gradle"

val github = "https://github.com/ethauvin/semver-gradle"
val packageName = "net.thauvin.erik.gradle.semver"

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(gradleApi())

    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib"))

    //testImplementation(gradleTestKit())

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            // Gradle 4.6
            apiVersion = "1.2"
        }
    }

    withType<Test> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }

    jacoco {
        toolVersion = "0.8.7"
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            html.required.set(true)
            xml.required.set(true)
        }
    }

    "sonarqube" {
        dependsOn(jacocoTestReport)
    }
}

detekt {
    //toolVersion = "main-SNAPSHOT"
    baseline = project.rootDir.resolve("detekt-baseline.xml")
}

sonarqube {
    properties {
        property("sonar.projectKey", "ethauvin_semver-gradle")
        property("sonar.organization", "ethauvin-github")
        property("sonar.host.url", "https://sonarcloud.io")
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
