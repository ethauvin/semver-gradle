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
import org.spekframework.spek2.style.gherkin.Feature
import kotlin.test.assertEquals

@Suppress("unused")
object SemverVersionSpec : Spek({
    Feature("SemverVersion") {
        val version = Version()
        Scenario("Testing Versions") {
            When("validating default version") {}

            Then("major should be 1") {
                assertEquals(1, version.major)
            }

            Then("minor should be 1") {
                assertEquals(0, version.minor)
            }

            Then("patch should be 0") {
                assertEquals(0, version.patch)
            }

            Then("prerelease should be empty") {
                assertEquals("", version.preRelease)
            }

            Then("meta should be empty") {
                assertEquals("", version.buildMeta)
            }

            Then("preRelease prefix should be -") {
                assertEquals("-", version.preReleasePrefix)
            }

            Then("meta prefix should be +") {
                assertEquals("+", version.buildMetaPrefix)
            }

            Then("separator should be .") {
                assertEquals(".", version.separator)
            }

            Then("version should be 1.0.0") {
                assertEquals("1.0.0", version.semver)
            }

            When("incrementing major") {
                version.increment(isMajor = true)
            }

            Then("should return 2.0.0") {
                assertEquals("2.0.0", version.semver)
            }

            When("incrementing minor") {
                version.increment(isMinor = true)
            }

            Then("should return 2.1.0") {
                assertEquals("2.1.0", version.semver)
            }

            When("incrementing patch") {
                version.increment(isPatch = true)
            }

            Then("should return 2.1.1") {
                assertEquals("2.1.1", version.semver)
            }

            When("incrementing minor again") {
                version.increment(isMinor = true)
            }

            Then("should return 2.2.0") {
                assertEquals("2.2.0", version.semver)
            }

            When("incrementing major again") {
                version.increment(isMajor = true)
            }

            Then("should return 3.0.0") {
                assertEquals("3.0.0", version.semver)
            }

            When("incrementing all") {
                version.increment(isMajor = true, isMinor = true, isPatch = true)
            }

            Then("should return 4.1.1") {
                assertEquals("4.1.1", version.semver)
            }

            When("incrementing major and minor") {
                version.increment(isMajor = true, isMinor = true)
            }

            Then("should return 5.1.0") {
                assertEquals("5.1.0", version.semver)
            }

            When("incrementing minor and patch") {
                version.increment(isMinor = true, isPatch = true)
            }
            Then("should return 5.2.1") {
                assertEquals("5.2.1", version.semver)
            }

            When("incrementing nothing") {
                version.increment()
            }
            Then("should still return 5.2.1") {
                assertEquals("5.2.1", version.semver)
            }

            When("resetting version") {
                version.major = 1
                version.minor = 0
                version.patch = 0
            }

            Then("should return 1.0.0") {
                assertEquals("1.0.0", version.semver)
            }

            When("adding prerelease") {
                version.preRelease = "beta"
            }

            Then("should return 1.0.0-beta") {
                assertEquals("1.0.0-beta", version.semver)
            }

            When("adding metadata") {
                version.buildMeta = "007"
            }

            Then("should return 1.0.0-beta+007") {
                assertEquals("1.0.0-beta+007", version.semver)
            }

            When("changing prerelease prefix") {
                version.preReleasePrefix = "--"
            }

            Then("should return 1.0.0--beta+007") {
                assertEquals("1.0.0--beta+007", version.semver)
            }

            When("changing meta prefix") {
                version.buildMetaPrefix = "++"
            }

            Then("should return 1.0.0--beta++007") {
                assertEquals("1.0.0--beta++007", version.semver)
            }

            When("changing separator") {
                version.separator = "-"
            }

            Then("should return 1-0-0--beta++007") {
                assertEquals("1-0-0--beta++007", version.semver)
            }
        }
    }
})
