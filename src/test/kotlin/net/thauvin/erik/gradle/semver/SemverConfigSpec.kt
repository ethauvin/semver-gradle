/*
 * SemverConfigSpec.kt
 *
 * Copyright (c) 2018-2019, Erik C. Thauvin (erik@thauvin.net)
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

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("unused")
object SemverConfigSpec : Spek({
    describe("config test") {
        given("a config") {
            val config = SemverConfig()
            val vars = listOf(
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
                SemverConfig.DEFAULT_MAJOR_KEY,
                SemverConfig.DEFAULT_MINOR_KEY,
                SemverConfig.DEFAULT_PATCH_KEY,
                SemverConfig.DEFAULT_PRERELEASE_KEY,
                SemverConfig.DEFAULT_PRERELEASE_PREFIX_KEY,
                SemverConfig.DEFAULT_BUILDMETA_KEY,
                SemverConfig.DEFAULT_BUILDMETA_PREFIX_KEY,
                SemverConfig.DEFAULT_SEPARATOR
            )
            on("defaults") {
                defaults.forEachIndexed { i, d ->
                    it("should be the same: ${vars[i]}, ${config.keysPrefix}$d") {
                        assertEquals(vars[i], "${config.keysPrefix}$d")
                    }
                }
            }
            on("should be version.properties") {
                assertEquals(config.properties, "version.properties")
            }
            on("set keys to test.xxx") {
                config.keysPrefix = "test."
                val keys = listOf(
                    config.majorKey,
                    config.minorKey,
                    config.patchKey,
                    config.preReleaseKey,
                    config.preReleasePrefixKey,
                    config.buildMetaKey,
                    config.buildMetaPrefixKey,
                    config.separatorKey)

                keys.forEach { k ->
                    it("should all start with test.xxx: $k") {
                        assertTrue(k.startsWith("test."))
                    }
                }
            }
        }
    }
})
