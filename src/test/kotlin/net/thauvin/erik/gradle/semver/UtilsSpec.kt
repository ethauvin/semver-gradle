/*
 * UtilsSpec.kt
 *
 * Copyright (c) 2018-2020, Erik C. Thauvin (erik@thauvin.net)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *   Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *   Neither the name of this project nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.thauvin.erik.gradle.semver

import net.thauvin.erik.gradle.semver.Utils.canReadFile
import org.gradle.api.GradleException
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.io.File
import java.util.Properties
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("unused")
object UtilsSpec : Spek({
    Feature("Utils") {
        val version = Version()
        val config = SemverConfig(version)
        val propsFile = File("test.properties")
        val projectDir = File("./")
        lateinit var props: Properties

        Scenario("Save/Load Properties") {
            When("saving the property") {
                config.properties = propsFile.name
                Utils.saveProperties(projectDir, config, version)
            }

            Then("properties file should exists and be readable") {
                assertEquals(propsFile.canReadFile(), propsFile.canRead() && propsFile.isFile)
            }

            When("loading the properties file") {
                props = Utils.loadProperties(propsFile)
                propsFile.delete()
            }

            Then("version and properties should be the same.") {
                assertEquals(props.getProperty(config.majorKey), version.major.toString(), "Major")
                assertEquals(props.getProperty(config.minorKey), version.minor.toString(), "Minor")
                assertEquals(props.getProperty(config.patchKey), version.patch.toString(), "Patch")
                assertEquals(props.getProperty(config.preReleaseKey), version.preRelease, "PreRelease")
                assertNull(props.getProperty(config.preReleasePrefixKey), "PreRelease Prefix")
                assertEquals(props.getProperty(config.buildMetaKey), version.buildMeta, "Build Meta")
                assertNull(props.getProperty(config.buildMetaPrefixKey), "Build Meta Prefix")
                assertNull(props.getProperty(config.separatorKey), "Separator")
                assertEquals(props.getProperty(config.semverKey), version.semver, "semver")
            }
        }

        Scenario("System Properties") {
            lateinit var sysProps: Array<Pair<String, String>>

            Given("new system properties") {
                sysProps = arrayOf(
                    Pair(config.majorKey, "2"),
                    Pair(config.minorKey, "1"),
                    Pair(config.patchKey, "1"),
                    Pair(config.preReleaseKey, "beta"),
                    Pair(config.buildMetaKey, "007")
                )
            }

            Then("none should already exists") {
                assertTrue(
                    Utils.isNotSystemProperty(
                        setOf(
                            config.majorKey, config.minorKey, config.patchKey, config.preReleaseKey,
                            config.buildMetaKey
                        )
                    )
                )
            }

            Then("version should match system properties") {
                sysProps.forEach {
                    System.getProperties().setProperty(it.first, it.second)
                    if (it.first == config.majorKey || it.first == config.minorKey || it.first == config.patchKey) {
                        assertEquals(Utils.loadIntProperty(props, it.first, -1), it.second.toInt())
                    } else {
                        assertEquals(Utils.loadProperty(props, it.first, ""), it.second)
                    }
                }
            }

            When("loading version") {
                Utils.loadVersion(config, version, props)
            }

            Then("version should be identical") {
                assertEquals(version.semver, "2.1.1-beta+007")
            }

            When("saving properties") {
                Utils.saveProperties(projectDir, config, version)
            }

            lateinit var newProps: Properties

            And("loading properties file") {
                newProps = Utils.loadProperties(propsFile)
            }

            Then("new properties should validate") {
                sysProps.forEach {
                    assertEquals(newProps.getProperty(it.first), it.second, it.second)
                }
                propsFile.delete()
            }

            When("setting the version as system property") {
                System.getProperties().setProperty(config.semverKey, "3.2.2")
            }

            And("loading the properties") {
                Utils.loadVersion(config, version, props)
            }

            Then("versions should match") {
                assertEquals(version.semver, System.getProperty(config.semverKey))
            }
        }

        Scenario("Version Parsing") {
            When("validating version parsing") {}

            Then("versions should parse") {
                listOf(
                    "1.0.0",
                    "2.1.0-beta",
                    "3.2.1-beta+007",
                    "4.3.2+007",
                    "11.11.1",
                    "111.11.11-beta",
                    "1111.111.11-beta+001.12"
                ).forEach {
                    assertTrue(Utils.parseSemVer(it, version), "parsing semver: $it")
                    assertEquals(it, version.semver, it)
                }
            }

            Then("should throw exceptions") {
                assertFailsWith<GradleException>("2.1.1a") {
                    Utils.parseSemVer("2.1.1a", version)
                }
                assertFailsWith<GradleException>("2a.1.1") {
                    Utils.parseSemVer("2a.1.1", version)
                }
                assertFailsWith<GradleException>("2.1a.1") {
                    Utils.parseSemVer("2.1a.1", version)
                }
                assertFailsWith<GradleException>("2.1") {
                    Utils.parseSemVer("2.1", version)
                }
            }

            Given("new prefixes") {
                version.preReleasePrefix = "."
                version.buildMetaPrefix = "."
            }

            Then("prefixes should parse") {
                listOf("2.1.0.beta.1", "2.1.1.1", "3.2.1.beta.1.007").forEach {
                    assertTrue(Utils.parseSemVer(it, version), "parsing semver: $it")
                    assertEquals(it, version.semver, it)
                }
            }

            Then("last pre-release and meta should match") {
                assertEquals(version.preRelease, "beta")
                assertEquals(version.buildMeta, "1.007")
            }
        }

        Scenario("Load locked properties") {
            lateinit var locked: File

            Given("the locked location") {
                locked = File("locked")
            }

            Then("loading locked properties") {
                assertFailsWith<GradleException> {
                    Utils.loadProperties(File(locked, propsFile.name))
                }
                locked.delete()
            }
        }

        Scenario("Save to locked properties") {
            lateinit var propsLocked: File
            Given("the locked properties") {
                propsLocked = File("locked.properties")
                propsLocked.createNewFile()
                propsLocked.setReadOnly()
                config.properties = propsLocked.name
            }

            Then("saving the locked properties file") {
                assertFailsWith<GradleException> {
                    Utils.saveProperties(projectDir, config, version)
                }
                propsLocked.delete()
            }
        }

        Scenario("Save/Load Properties in foo") {
            lateinit var fooDir: File
            lateinit var fooProps: File
            When("saving the foo property") {
                fooDir = File("foo")
                fooDir.mkdir()
                fooProps = File(fooDir, propsFile.name)
                config.properties = fooProps.absolutePath
                Utils.saveProperties(projectDir, config, version)
            }

            Then("foo properties file should exists and be readable") {
                assertEquals(fooProps.canReadFile(), fooProps.canRead() && fooProps.isFile)
            }

            When("loading the foo properties file") {
                props = Utils.loadProperties(fooProps)
                fooProps.delete()
                fooDir.delete()
            }

            Then("version in foo properties should be the same") {
                assertEquals(props.getProperty(config.semverKey), version.semver)
            }
        }
    }
})
