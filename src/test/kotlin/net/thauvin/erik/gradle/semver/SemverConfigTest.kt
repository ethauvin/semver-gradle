/*
 * SemverConfigTest.kt
 *
 * Copyright (c) 2018-2022, Erik C. Thauvin (erik@thauvin.net)
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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SemverConfigTest {
    private val config = SemverConfig(Version())

    @Test
    fun testConfigs() {
        val vars = listOf(
            config.semverKey,
            config.majorKey,
            config.minorKey,
            config.patchKey,
            config.preReleaseKey,
            config.preReleasePrefixKey,
            config.buildMetaKey,
            config.buildMetaPrefixKey,
            config.separatorKey
        )
        val defaults = listOf(
            SemverConfig.DEFAULT_SEMVER_KEY,
            SemverConfig.DEFAULT_MAJOR_KEY,
            SemverConfig.DEFAULT_MINOR_KEY,
            SemverConfig.DEFAULT_PATCH_KEY,
            SemverConfig.DEFAULT_PRERELEASE_KEY,
            SemverConfig.DEFAULT_PRERELEASE_PREFIX_KEY,
            SemverConfig.DEFAULT_BUILDMETA_KEY,
            SemverConfig.DEFAULT_BUILDMETA_PREFIX_KEY,
            SemverConfig.DEFAULT_SEPARATOR
        )

        defaults.forEachIndexed { i, d ->
            assertEquals(vars[i], "${config.keysPrefix}$d", " ${vars[i]} should be the same: ${config.keysPrefix}$d")
        }

        assertEquals(config.properties, "version.properties", "config.properties should be version.properties")

        assertTrue(
            config.toString().contains("properties='${SemverConfig.DEFAULT_PROPERTIES}'"),
            "toString contains default properties"
        )
    }

    @Test
    fun testExtensionProperties() {
        config.keysPrefix = "test."

        val newKeys = listOf(
            config.semverKey,
            config.majorKey,
            config.minorKey,
            config.patchKey,
            config.preReleaseKey,
            config.preReleasePrefixKey,
            config.buildMetaKey,
            config.buildMetaPrefixKey,
            config.separatorKey
        )

        newKeys.forEach { k ->
            assertTrue(k.startsWith("test."), "$k: all config keys should start with test.xxxx")
        }

        val defaultSemver =
            "${Version.DEFAULT_MAJOR}${Version.DEFAULT_SEPARATOR}${Version.DEFAULT_MINOR}" +
                    "${Version.DEFAULT_SEPARATOR}${Version.DEFAULT_PATCH}"
        assertEquals(config.semver, defaultSemver, "semver should be defaults")
        assertEquals(
            "${config.major}${config.separator}${config.minor}${config.separator}${config.patch}",
            defaultSemver,
            "major-minor-patch should be defaults."
        )
        assertEquals(config.preRelease, Version.DEFAULT_EMPTY, "preRelease empty default")
        assertEquals(config.buildMeta, Version.DEFAULT_EMPTY, "buildMeta empty default")
        assertEquals(config.preReleasePrefix, Version.DEFAULT_PRERELEASE_PREFIX, "preReleasePrefix default")
        assertEquals(config.buildMetaPrefix, Version.DEFAULT_BUILDMETA_PREFIX, "buildMetaPrefix default")

        assertEquals(config.semver, config.version, "semver = version")
    }
}
