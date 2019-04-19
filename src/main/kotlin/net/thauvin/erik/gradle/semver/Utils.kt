/*
 * Utils.kt
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

import org.gradle.api.GradleException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Properties

/**
 * The <code>Utils</code> class.
 *
 * @author <a href="https://erik.thauvin.net/" target="_blank">Erik C. Thauvin</a>
 * @created 2019-04-10
 * @since 1.0
 */
object Utils {
    fun File.canReadFile(): Boolean {
        return canRead() && isFile
    }

    private fun Int.length() = when (this) {
        0 -> 1
        else -> Math.log10(Math.abs(toDouble())).toInt() + 1
    }

    private fun Properties.put(key: String, value: String, isValidCondition: Boolean) {
        if (isValidCondition) put(key, value)
    }

    fun isNotSystemProperty(keys: Set<String>): Boolean {
        keys.forEach {
            if (System.getProperties().containsKey(it)) return false
        }
        return true
    }

    fun loadProperties(file: File): Properties {
        var isNew = false
        val props = Properties()
        file.apply {
            if (!exists()) {
                if (!createNewFile()) {
                    throw GradleException("Unable to create: `$absoluteFile`")
                } else {
                    isNew = true
                }
            }
            if (canReadFile()) {
                FileInputStream(this).reader().use { reader ->
                    props.apply {
                        if (!isNew) {
                            load(reader)
                        }
                    }
                }
            } else {
                throw GradleException("Unable to read version from: `$absoluteFile`")
            }
        }
        return props
    }

    fun loadIntProperty(props: Properties, key: String, default: Int): Int {
        try {
            return loadProperty(props, key, default.toString()).toInt()
        } catch (e: java.lang.NumberFormatException) {
            throw GradleException("Unable to parse $key property. (${e.message})", e)
        }
    }

    fun loadProperty(props: Properties, key: String, default: String): String {
        return System.getProperty(key, if (props.isNotEmpty()) props.getProperty(key, default) else default)
    }

    fun loadVersion(config: SemverConfig, version: Version, props: Properties) {
        props.apply {
            if (!parseSemVer(System.getProperty(config.semverKey), version)) {
                version.major = loadIntProperty(this, config.majorKey, Version.DEFAULT_MAJOR)
                version.minor = loadIntProperty(this, config.minorKey, Version.DEFAULT_MINOR)
                version.patch = loadIntProperty(this, config.patchKey, Version.DEFAULT_PATCH)
                version.preRelease = loadProperty(this, config.preReleaseKey, Version.DEFAULT_EMPTY)
                version.buildMeta = loadProperty(this, config.buildMetaKey, Version.DEFAULT_EMPTY)
            }

            if (!isEmpty) {
                version.preReleasePrefix =
                    getProperty(config.preReleasePrefixKey, Version.DEFAULT_PRERELEASE_PREFIX)
                version.buildMetaPrefix =
                    getProperty(config.buildMetaPrefixKey, Version.DEFAULT_BUILDMETA_PREFIX)
                version.separator = getProperty(config.separatorKey, Version.DEFAULT_SEPARATOR)
            }
        }
    }

    fun parseSemVer(input: String?, version: Version): Boolean {
        if (input.isNullOrBlank()) return false

        var semver = StringBuilder(input)
        var start = semver.indexOf(version.separator)
        var minor = -1
        var major = -1
        var patch = -1
        var preRelease = ""
        var buildMeta = ""

        try {
            // major
            if (start != -1) {
                major = Math.abs(semver.substring(0, start).toInt())
                semver.delete(0, start + major.length())
                start = semver.indexOf(version.separator)
                // minor
                if (start != -1) {
                    minor = Math.abs(semver.substring(0, start).toInt())
                    semver = semver.delete(0, start + minor.length())
                    start = semver.indexOf(version.preReleasePrefix)
                    // patch
                    if (start != -1) {
                        patch = Math.abs(semver.substring(0, start).toInt())
                        semver.delete(0, start + minor.length())
                        start = semver.lastIndexOf(version.buildMetaPrefix)
                        // pre-release
                        if (start != -1) {
                            preRelease = semver.substring(0, start)
                            semver.delete(0, preRelease.length)
                            start = semver.indexOf(version.buildMetaPrefix)
                            // build meta
                            if (start != -1) {
                                buildMeta = semver.substring(version.preReleasePrefix.length)
                                semver.clear()
                            }
                        } else {
                            // no build meta
                            preRelease = semver.toString()
                            semver.clear()
                        }
                    } else if (semver.isNotEmpty()) {
                        // no pre-release
                        start = semver.lastIndexOf(version.buildMetaPrefix)
                        // patch & build meta
                        if (start != -1) {
                            patch = semver.substring(0, start).toInt()
                            semver.delete(0, start + minor.length())
                            buildMeta = semver.toString()
                        } else {
                            // patch
                            patch = semver.toString().toInt()
                        }
                        semver.clear()
                    }
                }
            }
        } catch (e: NumberFormatException) {
            throw GradleException("Unable to parse version: \"$input\" (${e.message})", e)
        }

        if (semver.isNotEmpty()) throw GradleException("Unable to parse version: \"$input\".")

        version.major = major
        version.minor = minor
        version.patch = patch
        version.preRelease = preRelease
        version.buildMeta = buildMeta

        return true
    }

    fun saveProperties(config: SemverConfig, version: Version) {
        val propsFile = File(config.properties)
        SortedProperties().apply {
            propsFile.apply {
                if (canReadFile()) {
                    FileInputStream(this).reader().use { load(it) }
                } else {
                    createNewFile()
                }

                put(config.semverKey, version.semver)
                put(config.majorKey, version.major.toString())
                put(config.minorKey, version.minor.toString())
                put(config.patchKey, version.patch.toString())
                put(config.preReleaseKey, version.preRelease)
                put(config.buildMetaKey, version.buildMeta)
                put(config.semverKey, version.semver)

                put(config.buildMetaPrefixKey, version.buildMetaPrefix,
                    version.buildMetaPrefix != Version.DEFAULT_BUILDMETA_PREFIX ||
                        containsKey(config.buildMetaPrefixKey))
                put(config.preReleasePrefixKey, version.preReleasePrefix,
                    version.preReleasePrefix != Version.DEFAULT_PRERELEASE_PREFIX ||
                        containsKey(config.preReleasePrefixKey))
                put(config.separatorKey, version.separator,
                    version.separator != Version.DEFAULT_SEPARATOR ||
                        containsKey(config.separatorKey))

                if (canWrite()) {
                    FileOutputStream(this).writer().use {
                        store(it, "Generated by the Semver Plugin for Gradle")
                    }
                } else {
                    throw GradleException("Unable to write version to: `$absoluteFile`")
                }
            }
        }
    }
}
