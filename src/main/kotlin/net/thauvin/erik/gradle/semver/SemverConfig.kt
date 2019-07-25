/*
 * SemverConfig.kt
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

import javax.inject.Inject

open class SemverConfig @Inject constructor(
    private val semVersion: Version
) {
    companion object {
        const val DEFAULT_KEYS_PREFIX = "version."
        const val DEFAULT_PROPERTIES = "${DEFAULT_KEYS_PREFIX}properties"
        const val DEFAULT_SEMVER_KEY = "semver"
        const val DEFAULT_MAJOR_KEY = "major"
        const val DEFAULT_MINOR_KEY = "minor"
        const val DEFAULT_PATCH_KEY = "patch"
        const val DEFAULT_PRERELEASE_KEY = "prerelease"
        const val DEFAULT_PRERELEASE_PREFIX_KEY = "prerelease.prefix"
        const val DEFAULT_BUILDMETA_KEY = "buildmeta"
        const val DEFAULT_BUILDMETA_PREFIX_KEY = "buildmeta.prefix"
        const val DEFAULT_SEPARATOR = "separator"
    }

    var properties = DEFAULT_PROPERTIES

    // Keys
    var semverKey = DEFAULT_SEMVER_KEY
        get() = "$keysPrefix$field"
    var majorKey = DEFAULT_MAJOR_KEY
        get() = "$keysPrefix$field"
    var minorKey = DEFAULT_MINOR_KEY
        get() = "$keysPrefix$field"
    var patchKey = DEFAULT_PATCH_KEY
        get() = "$keysPrefix$field"
    var preReleaseKey = DEFAULT_PRERELEASE_KEY
        get() = "$keysPrefix$field"
    var preReleasePrefixKey = DEFAULT_PRERELEASE_PREFIX_KEY
        get() = "$keysPrefix$field"
    var buildMetaKey = DEFAULT_BUILDMETA_KEY
        get() = "$keysPrefix$field"
    var buildMetaPrefixKey = DEFAULT_BUILDMETA_PREFIX_KEY
        get() = "$keysPrefix$field"
    var separatorKey = DEFAULT_SEPARATOR
        get() = "$keysPrefix$field"
    var keysPrefix = DEFAULT_KEYS_PREFIX

    // Properties
    val semver: String
        get() = semVersion.semver
    val version: String
        get() = semVersion.semver
    val major: Int
        get() = semVersion.major
    val minor: Int
        get() = semVersion.minor
    val patch: Int
        get() = semVersion.patch
    val preRelease: String
        get() = semVersion.preRelease
    val buildMeta: String
        get() = semVersion.buildMeta
    val preReleasePrefix: String
        get() = semVersion.preReleasePrefix
    val buildMetaPrefix: String
        get() = semVersion.buildMetaPrefix
    val separator: String
        get() = semVersion.separator

    override fun toString(): String {
        return "SemverConfig(" +
            "properties='$properties', " +
            "semverKey='$semverKey', " +
            "majorKey='$majorKey', " +
            "minorKey='$minorKey', " +
            "patchKey='$patchKey', " +
            "preReleaseKey='$preReleaseKey', " +
            "preReleasePrefixKey='$preReleasePrefixKey', " +
            "buildMetaKey='$buildMetaKey', " +
            "buildMetaPrefixKey='$buildMetaPrefixKey', " +
            "separator='$separatorKey', " +
            "keysPrefix='$keysPrefix', " +
            "semver='$semver', " +
            "major='$major', " +
            "minor='$minor', " +
            "patch='$patch', " +
            "preRelease='$preRelease', " +
            "buildMeta='$buildMeta', " +
            "preReleasePrefix='$preReleasePrefix', " +
            "buildMetaPrefix='$buildMetaPrefix', " +
            "separator='$separator'" +
            ')'
    }
}
