/*
 * SemverVersionTest.kt
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

import kotlin.test.Test
import kotlin.test.assertEquals

class VersionTest {
    private val version = Version()

    @Test
    fun testDefaultVersion() {
        assertEquals(1, version.major, "major should be 1")
        assertEquals(0, version.minor, "minor should be 1")
        assertEquals(0, version.patch, "patch should be 0")
        assertEquals("", version.preRelease, "prerelease should be empty")
        assertEquals("", version.buildMeta, "meta should be empty")
        assertEquals("-", version.preReleasePrefix, "preRelease prefix should be -")
        assertEquals("+", version.buildMetaPrefix, "meta prefix should be +")
        assertEquals(".", version.separator, "separator should be .")
        assertEquals("1.0.0", version.semver, "version should be 1.0.0")
        assertEquals(version.toString(), version.semver, "toString should be semver")
    }

    @Test
    fun testIncrement() {
        version.increment(isMajor = true)
        assertEquals("2.0.0", version.semver, "should return 2.0.0")

        version.increment(isMinor = true)
        assertEquals("2.1.0", version.semver, "should return 2.1.0")

        version.increment(isPatch = true)
        assertEquals("2.1.1", version.semver, "should return 2.1.1")

        version.increment(isMinor = true)
        assertEquals("2.2.0", version.semver, "should return 2.2.0")

        version.increment(isMajor = true)
        assertEquals("3.0.0", version.semver, "should return 3.0.0")

        version.increment(isMajor = true, isMinor = true, isPatch = true)
        assertEquals("4.1.1", version.semver, "should return 4.1.1")

        version.increment(isMajor = true, isMinor = true)
        assertEquals("5.1.0", version.semver, "should return 5.1.0")

        version.increment(isMinor = true, isPatch = true)
        assertEquals("5.2.1", version.semver, "should return 5.2.1")

        version.increment()
        assertEquals("5.2.1", version.semver, "should still return 5.2.1")
    }

    @Test
    fun testVersion() {
        version.major = 1
        version.minor = 0
        version.patch = 0
        assertEquals("1.0.0", version.semver, "should return 1.0.0")

        version.preRelease = "beta"
        assertEquals("1.0.0-beta", version.semver, "should return 1.0.0-beta")

        version.buildMeta = "007"
        assertEquals("1.0.0-beta+007", version.semver, "should return 1.0.0-beta+007")

        version.preReleasePrefix = "--"
        assertEquals("1.0.0--beta+007", version.semver, "should return 1.0.0--beta+007")

        version.buildMetaPrefix = "++"
        assertEquals("1.0.0--beta++007", version.semver, "should return 1.0.0--beta++007")

        version.separator = "-"
        assertEquals("1-0-0--beta++007", version.semver, "should return 1-0-0--beta++007")

        assertEquals(version.toString(), version.semver, "toString() should return semver")
    }
}
