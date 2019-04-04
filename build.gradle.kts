import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.JUnitPlatformExtension

buildscript {
    dependencies {
        classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.0")
    }
}

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    jacoco
    id("com.github.ben-manes.versions").version("0.21.0")
    id("com.gradle.plugin-publish").version("0.10.1")
    id("io.gitlab.arturbosch.detekt").version("1.0.0-RC14")
    id("org.jlleitschuh.gradle.ktlint").version("7.2.1")
    id("org.sonarqube") version "2.7"
}

version = "1.0.0"
group = "net.thauvin.erik.gradle"

var github = "https://github.com/ethauvin/semver-gradle"
var packageName = "net.thauvin.erik.gradle.semver"

var spekVersion = "1.2.1"

repositories {
    jcenter()
}

apply {
    plugin("org.junit.platform.gradle.plugin")
}

dependencies {
    implementation(gradleApi())

    testImplementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(gradleTestKit())

    testImplementation("org.jetbrains.spek:spek-api:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testRuntimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.junit.platform")
    }

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.5.0-M1") {
        because("Needed to run tests IDEs that bundle an older version")
    }
}

configure<JUnitPlatformExtension> {
    enableStandardTestTask = true
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
        // Gradle 4.6
        kotlinOptions.apiVersion = "1.2"
    }

    withType<Test> {
        useJUnitPlatform {
            includeEngines("spek")
        }
    }

    withType<JacocoReport> {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
        }
    }

    "check" {
        dependsOn("ktlintCheck")
    }

    "sonarqube" {
        dependsOn("jacocoTestReport")
    }
}

detekt {
    input = files("src/main/kotlin")
    filters = ".*/resources/.*,.*/build/.*"
    baseline = project.rootDir.resolve("detekt-baseline.xml")
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
