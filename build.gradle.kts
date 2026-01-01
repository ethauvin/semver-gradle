import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.github.ben-manes.versions") version "0.53.0"
    id("com.gradle.plugin-publish") version "2.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("java-gradle-plugin")
    id("java")
    id("maven-publish")
    id("org.gradle.kotlin.kotlin-dsl") version "6.4.1"
    kotlin("jvm") version "2.3.0"
}

version = "1.0.5-SNAPSHOT"
group = "net.thauvin.erik.gradle"

val github = "https://github.com/ethauvin/semver-gradle"
val packageName = "net.thauvin.erik.gradle.semver"

repositories {
    mavenCentral()
    maven { url = uri("https://central.sonatype.com/repository/maven-snapshots/") }
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.3.0"))
    implementation(gradleApi())


    // testImplementation(gradleTestKit())

    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

kotlin {
    jvmToolchain(17)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
            events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
}

detekt {
    toolVersion = "main-SNAPSHOT"
    baseline = project.rootDir.resolve("detekt-baseline.xml")
}

tasks.withType<Detekt>().configureEach {
    if (JavaVersion.current() >= JavaVersion.VERSION_24) {
        jvmTarget = "23"
    }
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
