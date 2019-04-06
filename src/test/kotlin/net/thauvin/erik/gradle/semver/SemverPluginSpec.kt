package net.thauvin.erik.gradle.semver

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.File
import java.nio.file.Files
import java.util.Properties
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("unused")
object SemverPluginSpec : Spek({
    describe("a config and version") {
        val version by memoized { Version() }
        val config by memoized { SemverConfig() }
        val configFile = File("test.properties")

        config.properties = configFile.name

        describe("test save properties") {
            it("should save properties") {
                SemverPlugin.saveProperties(config, version)
                assertTrue(configFile.exists())
            }
        }
        describe("validate the properties file") {
            it("verion should be the same") {
                val props = Properties().apply {
                    Files.newInputStream(configFile.toPath()).use { nis ->
                        load(nis)
                        configFile.delete()
                    }
                }

                assertEquals(props.getProperty(config.majorKey), version.major, "Major")
                assertEquals(props.getProperty(config.minorKey), version.minor, "Minor")
                assertEquals(props.getProperty(config.patchKey), version.patch, "Patch")
                assertEquals(props.getProperty(config.preReleaseKey), version.preRelease, "PreRelease.")
                assertNull(props.getProperty(config.preReleasePrefixKey), "PreRelease Prefix")
                assertEquals(props.getProperty(config.buildMetaKey), version.buildMeta, "Build Meta")
                assertNull(props.getProperty(config.buildMetaPrefixKey), "Build Meta Prefix")
                assertNull(props.getProperty(config.separatorKey), "Separator")
            }
        }
    }
})
