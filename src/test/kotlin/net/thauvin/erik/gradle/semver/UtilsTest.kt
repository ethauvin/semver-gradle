/*
 * UtilsTest.kt
 *
 * Copyright (c) 2018-2021, Erik C. Thauvin (erik@thauvin.net)
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
import java.io.File
import kotlin.test.*

/**
 * [Utils] Tests
 */
class UtilsTest {
    private val version = Version()
    private val config = SemverConfig(version)
    private val propsFile = File("test.properties")
    private val projectDir = File("./")

    @Test
    fun testExceptions() {
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

    @Test
    fun testFooProperties() {
        val fooDir = File("foo")
        fooDir.mkdir()
        val fooFile = File(fooDir, propsFile.name)
        config.properties = fooFile.absolutePath
        Utils.saveProperties(projectDir, config, version)

        assertEquals(
            fooFile.canReadFile(),
            fooFile.canRead() && fooFile.isFile,
            "foo properties file should exists and be readable"
        )

        val fooProps = Utils.loadProperties(fooFile)
        fooFile.delete()
        fooDir.delete()

        assertEquals(
            fooProps.getProperty(config.semverKey),
            version.semver,
            "version in foo properties should be the same"
        )
    }

    @Test
    fun testLoadSaveProperties() {
        config.properties = propsFile.name
        Utils.saveProperties(projectDir, config, version)
        assertEquals(
            propsFile.canReadFile(),
            propsFile.canRead() && propsFile.isFile,
            "properties file should exists and be readable"
        )

        val props = Utils.loadProperties(propsFile)

        assertEquals(props.getProperty(config.majorKey), version.major.toString(), "Major")
        assertEquals(props.getProperty(config.minorKey), version.minor.toString(), "Minor")
        assertEquals(props.getProperty(config.patchKey), version.patch.toString(), "Patch")
        assertEquals(props.getProperty(config.preReleaseKey), version.preRelease, "PreRelease")
        assertNull(props.getProperty(config.preReleasePrefixKey), "PreRelease Prefix")
        assertEquals(props.getProperty(config.buildMetaKey), version.buildMeta, "Build Meta")
        assertNull(props.getProperty(config.buildMetaPrefixKey), "Build Meta Prefix")
        assertNull(props.getProperty(config.separatorKey), "Separator")
        assertEquals(props.getProperty(config.semverKey), version.semver, "semver")

        propsFile.delete()
    }

    @Test
    fun testLockedProperties() {
        var locked = File("locked")

        assertFailsWith<GradleException> {
            Utils.loadProperties(File(locked, propsFile.name))
        }
        locked.delete()

        locked = File(System.getProperty("user.home") + File.separator + "locked.properties")
        locked.createNewFile()
        locked.setReadOnly()
        config.properties = locked.name

        if (!locked.canWrite()) {
            assertFailsWith<GradleException> {
                Utils.saveProperties(locked.parentFile, config, version)
            }
        }
        locked.delete()
    }

    @Test
    fun testPrefix() {
        version.preReleasePrefix = "."
        version.buildMetaPrefix = "."

        listOf("2.1.0.beta.1", "2.1.1.1", "3.2.1.beta.1.007").forEach {
            assertTrue(Utils.parseSemVer(it, version), "parsing semver: $it")
            assertEquals(it, version.semver, it)
        }

        assertEquals(version.preRelease, "beta", "last pre-release should match")
        assertEquals(version.buildMeta, "1.007", "last meta should match")
    }

    @Test
    fun testSystemProperties() {
        val sysProps = arrayOf(
            Pair(config.majorKey, "2"),
            Pair(config.minorKey, "1"),
            Pair(config.patchKey, "1"),
            Pair(config.preReleaseKey, "beta"),
            Pair(config.buildMetaKey, "007")
        )

        assertTrue(
            Utils.isNotSystemProperty(
                setOf(
                    config.majorKey,
                    config.minorKey,
                    config.patchKey,
                    config.preReleaseKey,
                    config.buildMetaKey
                )
            ),
            "none should already exists"
        )

        val props = Utils.loadProperties(propsFile)

        sysProps.forEach {
            val msg = "${it.first} should match system properties"
            System.getProperties().setProperty(it.first, it.second)
            if (it.first == config.majorKey || it.first == config.minorKey || it.first == config.patchKey) {
                assertEquals(Utils.loadIntProperty(props, it.first, -1), it.second.toInt(), msg)
            } else {
                assertEquals(Utils.loadProperty(props, it.first, ""), it.second, msg)
            }
        }

        Utils.loadVersion(config, version, props)
        assertEquals(version.semver, "2.1.1-beta+007", "version should be identical")

        Utils.saveProperties(projectDir, config, version)

        val newPropsFile = File(config.properties)
        val newProps = Utils.loadProperties(newPropsFile)

        sysProps.forEach {
            assertEquals(newProps.getProperty(it.first), it.second, "new properties should validate")
        }

        newPropsFile.delete()

        System.getProperties().setProperty(config.semverKey, "3.2.2")
        Utils.loadVersion(config, version, props)
        assertEquals(version.semver, System.getProperty(config.semverKey), "versions should match")
    }

    @Test
    fun testVersionParsing() {
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
}
