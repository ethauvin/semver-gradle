/*
 * SemverVersionSpec.kt
 *
 * Copyright (c) 2018, Erik C. Thauvin (erik@thauvin.net)
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

@Suppress("unused")
object SemverVersionSpec : Spek({
    describe("version test") {
        given("a version") {
            val version = Version()
            on("increment major") {
                version.increment(isMajor = true)
                it("should return 2.0.0") {
                    assertEquals("2.0.0", version.semver)
                }
            }
            on("increment minor") {
                version.increment(isMinor = true)
                it("should return 2.1.0") {
                    assertEquals("2.1.0", version.semver)
                }
            }
            on("increment patch") {
                version.increment(isPatch = true)
                it("should return 2.1.1") {
                    assertEquals("2.1.1", version.semver)
                }
            }
            on("increment patch") {
                version.increment(isMinor = true)
                it("should return 2.1.0") {
                    assertEquals("2.1.0", version.semver)
                }
            }
            on("increment patch") {
                version.increment(isMajor = true)
                it("should return 3.0.0") {
                    assertEquals("3.0.0", version.semver)
                }
            }
            on("increment all") {
                version.increment(true, true, true)
                it("should return 3.1.1") {
                    assertEquals("3.1.1", version.semver)
                }
            }
            on("reset version") {
                version.major = "1"
                version.minor = "0"
                version.patch = "0"
                it("should return 1.0.0") {
                    assertEquals("1.0.0", version.semver)
                }
            }
            on("add prerelease") {
                version.preRelease = "beta"
                it("should return 1.0.0-beta") {
                    assertEquals("1.0.0-beta", version.semver)
                }
            }
            on("add metadata") {
                version.buildMeta = "007"
                it("should return 1.0.0-beta+007") {
                    assertEquals("1.0.0-beta+007", version.semver)
                }
            }
            on("change prerelease prefix") {
                version.preReleasePrefix = "--"
                it("should return 1.0.0--beta+007") {
                    assertEquals("1.0.0--beta+007", version.semver)
                }
            }
            on("change prerelease prefix") {
                version.buildMetaPrefix = "++"
                it("should return 1.0.0--beta+=007") {
                    assertEquals("1.0.0--beta++007", version.semver)
                }
            }
            on("change sperator") {
                version.separator = "-"
                it("should return 1-0-0--beta+007") {
                    assertEquals("1-0-0--beta+007", version.semver)
                }
            }
        }
    }
})
