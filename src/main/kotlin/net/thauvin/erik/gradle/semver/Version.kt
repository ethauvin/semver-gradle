/*
 * Version.kt
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

class Version {
    companion object {
        const val DEFAULT_MAJOR: String = "1"
        const val DEFAULT_MINOR: String = "0"
        const val DEFAULT_PATCH: String = "0"
        const val DEFAULT_EMPTY: String = ""
        const val DEFAULT_PRERELEASE_PREFIX = "-"
        const val DEFAULT_BUILDMETA_PREFIX = "+"
        const val DEFAULT_SEPARATOR = "."
    }

    var major = DEFAULT_MAJOR
    var minor = DEFAULT_MINOR
    var patch = DEFAULT_PATCH
    var preRelease = DEFAULT_EMPTY
    var preReleasePrefix = DEFAULT_PRERELEASE_PREFIX
    var buildMeta = DEFAULT_EMPTY
    var buildMetaPrefix = DEFAULT_BUILDMETA_PREFIX
    var separator = DEFAULT_SEPARATOR

    val semver: String
        get() = "$major$separator$minor$separator$patch" +
                (if (preRelease.isNotEmpty()) "$preReleasePrefix$preRelease" else "") +
                (if (buildMeta.isNotEmpty()) "$buildMetaPrefix$buildMeta" else "")

    fun increment(isMajor: Boolean = false, isMinor: Boolean = false, isPatch: Boolean = false) {
        if (isMajor) {
            major = (major.toInt() + 1).toString()
            minor = DEFAULT_MINOR
            patch = DEFAULT_PATCH
        }
        if (isMinor) {
            minor = (minor.toInt() + 1).toString()
            patch = DEFAULT_PATCH
        }
        if (isPatch) patch = (patch.toInt() + 1).toString()
    }
}
