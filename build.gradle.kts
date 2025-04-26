import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("com.github.ben-manes.versions") version "0.52.0"
    id("com.gradle.plugin-publish") version "1.3.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("java-gradle-plugin")
    id("java")
    id("maven-publish")
    id("org.gradle.kotlin.kotlin-dsl") version "5.2.0"
    kotlin("jvm") version "2.0.21"
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

    // testImplementation(gradleTestKit())

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_1_7)
    }
}

tasks {
    withType<Test> {
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

detekt {
    // toolVersion = "main-SNAPSHOT"
    baseline = project.rootDir.resolve("detekt-baseline.xml")
}

gradlePlugin {
    website.set(github)
    vcsUrl.set(github)

    plugins {
        create(project.name) {
            id = packageName
            displayName = "SemVer Plugin"
            description = "Semantic Version Plugin for Gradle"
            tags.set(listOf("semver", "semantic", "version", "versioning", "auto-increment", "kotlin", "java"))
            implementationClass = "$packageName.SemverPlugin"
        }
    }
}
