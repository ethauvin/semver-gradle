/*
 * SemverConfigSpec.kt
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

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("unused")
object SemverConfigSpec : Spek({
    Feature("SemverConfig") {
        val config = SemverConfig(Version())
        Scenario("Testing configs") {
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

            When("checking defaults") {}

            defaults.forEachIndexed { i, d ->
                Then(" ${vars[i]} should be the same: ${config.keysPrefix}$d") {
                    assertEquals(vars[i], "${config.keysPrefix}$d")
                }
            }

            Then("config.properties should be version.properties") {
                assertEquals(config.properties, "version.properties")
            }

            lateinit var newKeys: List<String>

            When("setting keyPrefix to test.") {
                config.keysPrefix = "test."
                newKeys = listOf(
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
            }

            Then("all config keys should start with test.xxxx") {
                newKeys.forEach { k ->
                    assertTrue(k.startsWith("test."), k)
                }
            }

            When("checking extension properties") {}

            Then("semver should be defaults") {
                val defaultSemver =
                    "${Version.DEFAULT_MAJOR}${Version.DEFAULT_SEPARATOR}${Version.DEFAULT_MINOR}${Version.DEFAULT_SEPARATOR}${Version.DEFAULT_PATCH}"
                assertEquals(config.semver, defaultSemver)
                assertEquals(
                    "${config.major}${config.separator}${config.minor}${config.separator}${config.patch}",
                    defaultSemver
                )
                assertEquals(config.preRelease, Version.DEFAULT_EMPTY)
                assertEquals(config.buildMeta, Version.DEFAULT_EMPTY)
                assertEquals(config.preReleasePrefix, Version.DEFAULT_PRERELEASE_PREFIX)
                assertEquals(config.buildMetaPrefix, Version.DEFAULT_BUILDMETA_PREFIX)
            }

            Then("semver = version") {
                assertEquals(config.semver, config.version)
            }
        }
    }
})
