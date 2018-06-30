import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.9.10"
    id("com.github.ben-manes.versions") version "0.20.0"
}

var shortName = "server"
var github = "https://github.com/ethauvin/gradle-semver-plugin"
var packageName = "net.thauvin.erik.gradle.semver"

version = "0.9.1"
group = "net.thauvin.erik.gradle"


var spekVersion = "1.1.5"

repositories {
    jcenter()
}

dependencies {
    compile(gradleApi())

    testImplementation(kotlin("reflect"))
    testImplementation(kotlin("test"))

    testImplementation("org.jetbrains.spek:spek-api:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
    }

    testRuntimeOnly("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion") {
        exclude(group = "org.jetbrains.kotlin")
        exclude(group = "org.junit.platform")
    }

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.2.0") {
        because("Needed to run tests IDEs that bundle an older version")
    }
    testImplementation(gradleTestKit())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    // Gradle 4.6
    kotlinOptions.apiVersion = "1.2"
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek")
    }
}

gradlePlugin {
    (plugins) {
        project.name {
            id = packageName
            implementationClass = "$packageName.SemverPlugin"
        }
    }
}

pluginBundle {
    website = github
    vcsUrl = github
    description = "Gradle plugin to automatically manage Semantic Version numbering."
    tags = listOf("semver", "version", "versioning")

    (plugins) {
        project.name {
            id = packageName
            displayName = "Gradle Semamtic Version Plugin"
        }
    }

    mavenCoordinates {
        groupId = project.group.toString()
        artifactId = project.name
    }
}
