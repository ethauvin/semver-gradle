/*
 * SemverVersionSpec.kt
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

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.test.assertEquals

@Suppress("unused")
object SemverVersionSpec : Spek({
    describe("a version") {
        val version = Version()
        describe("valdiate default version") {
            it("major should be 1") {
                assertEquals("1", version.major)
            }
            it("minor should be 1") {
                assertEquals("0", version.minor)
            }
            it("patch should be 0") {
                assertEquals("0", version.patch)
            }
            it("prerelease should be empty") {
                assertEquals("", version.preRelease)
            }
            it("meta should be empty") {
                assertEquals("", version.buildMeta)
            }
            it("preRelease prefix should be -") {
                assertEquals("-", version.preReleasePrefix)
            }
            it("meta prefix should be +") {
                assertEquals("+", version.buildMetaPrefix)
            }
            it("separator should be .") {
                assertEquals(".", version.separator)
            }
            it("version should be 1.0.0") {
                assertEquals("1.0.0", version.semver)
            }
        }
        describe("increment major") {
            it("should return 2.0.0") {
                version.increment(isMajor = true)
                assertEquals("2.0.0", version.semver)
            }
        }
        describe("increment minor") {
            it("should return 2.1.0") {
                version.increment(isMinor = true)
                assertEquals("2.1.0", version.semver)
            }
        }
        describe("increment patch") {
            it("should return 2.1.1") {
                version.increment(isPatch = true)
                assertEquals("2.1.1", version.semver)
            }
        }
        describe("increment minor again") {
            it("should return 2.2.0") {
                version.increment(isMinor = true)
                assertEquals("2.2.0", version.semver)
            }
        }
        describe("increment major again") {
            it("should return 3.0.0") {
                version.increment(isMajor = true)
                assertEquals("3.0.0", version.semver)
            }
        }
        describe("increment all") {
            it("should return 4.1.1") {
                version.increment(isMajor = true, isMinor = true, isPatch = true)
                assertEquals("4.1.1", version.semver)
            }
        }
        describe("increment major and minor") {
            it("should return 5.1.0") {
                version.increment(isMajor = true, isMinor = true)
                assertEquals("5.1.0", version.semver)
            }
        }
        describe("increment minor and patch") {
            it("should return 5.2.1") {
                version.increment(isMinor = true, isPatch = true)
                assertEquals("5.2.1", version.semver)
            }
        }
        describe("increment nothing") {
            it("should still return 5.2.1") {
                version.increment()
                assertEquals("5.2.1", version.semver)
            }
        }
        describe("reset version") {
            it("should return 1.0.0") {
                version.major = "1"
                version.minor = "0"
                version.patch = "0"
                assertEquals("1.0.0", version.semver)
            }
        }
        describe("add prerelease") {
            it("should return 1.0.0-beta") {
                version.preRelease = "beta"
                assertEquals("1.0.0-beta", version.semver)
            }
        }
        describe("add metadata") {
            it("should return 1.0.0-beta+007") {
                version.buildMeta = "007"
                assertEquals("1.0.0-beta+007", version.semver)
            }
        }
        describe("change prerelease prefix") {
            it("should return 1.0.0--beta+007") {
                version.preReleasePrefix = "--"
                assertEquals("1.0.0--beta+007", version.semver)
            }
        }
        describe("change meta prefix") {
            it("should return 1.0.0--beta++007") {
                version.buildMetaPrefix = "++"
                assertEquals("1.0.0--beta++007", version.semver)
            }
        }
        describe("change separator") {
            it("should return 1-0-0--beta++007") {
                version.separator = "-"
                assertEquals("1-0-0--beta++007", version.semver)
            }
        }
    }
})
