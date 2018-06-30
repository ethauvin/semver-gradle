/*
 * SemverConfig.kt
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

open class SemverConfig {
    companion object {
        const val DEFAULT_KEY_PREFIX = "version."
        const val DEFAULT_PROPERTIES = "${DEFAULT_KEY_PREFIX}properties"
        const val DEFAULT_MAJOR_KEY = "${DEFAULT_KEY_PREFIX}major"
        const val DEFAULT_MINOR_KEY = "${DEFAULT_KEY_PREFIX}minor"
        const val DEFAULT_PATCH_KEY = "${DEFAULT_KEY_PREFIX}patch"
        const val DEFAULT_PRERELEASE_KEY = "${DEFAULT_KEY_PREFIX}prerelease"
        const val DEFAULT_PRERELEASE_PREFIX_KEY = "${DEFAULT_KEY_PREFIX}prerelease.prefix"
        const val DEFAULT_BUILDMETA_KEY = "${DEFAULT_KEY_PREFIX}buildmeta"
        const val DEFAULT_BUILDMETA_PREFIX_KEY = "${DEFAULT_KEY_PREFIX}buildmeta.prefix"
        const val DEFAULT_SEPARATOR = "${DEFAULT_KEY_PREFIX}separator"
    }

    var properties = DEFAULT_PROPERTIES
    var majorKey = DEFAULT_MAJOR_KEY
    var minorKey = DEFAULT_MINOR_KEY
    var patchKey = DEFAULT_PATCH_KEY
    var preReleaseKey = DEFAULT_PRERELEASE_KEY
    var preReleasePrefixKey = DEFAULT_PRERELEASE_PREFIX_KEY
    var buildMetaKey = DEFAULT_BUILDMETA_KEY
    var buildMetaPrefixKey = DEFAULT_BUILDMETA_PREFIX_KEY
    var separatorKey = DEFAULT_SEPARATOR

    @Suppress("unused")
    fun setKeysPrefix(keyPrefix: String) {
        if (keyPrefix.isNotBlank()) {
            majorKey = keyPrefix + majorKey.removePrefix(DEFAULT_KEY_PREFIX)
            minorKey = keyPrefix + minorKey.removePrefix(DEFAULT_KEY_PREFIX)
            patchKey = keyPrefix + patchKey.removePrefix(DEFAULT_KEY_PREFIX)
            preReleaseKey = keyPrefix + preReleaseKey.removePrefix(DEFAULT_KEY_PREFIX)
            preReleasePrefixKey = keyPrefix + preReleasePrefixKey.removePrefix(DEFAULT_KEY_PREFIX)
            buildMetaKey = keyPrefix + buildMetaKey.removePrefix(DEFAULT_KEY_PREFIX)
            buildMetaPrefixKey = keyPrefix + buildMetaPrefixKey.removePrefix(DEFAULT_KEY_PREFIX)
            separatorKey = keyPrefix + separatorKey.removePrefix(DEFAULT_KEY_PREFIX)
        }
    }
}