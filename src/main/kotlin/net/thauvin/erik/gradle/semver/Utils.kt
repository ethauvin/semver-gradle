/*
 * Utils.kt
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

import org.gradle.api.GradleException
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
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

    private fun Properties.put(key: String, value: String, isValidCondition: Boolean) {
        if (isValidCondition) put(key, value)
    }

    fun isNotSystemProperty(keys: Set<String>): Boolean {
        keys.forEach {
            if (System.getProperties().containsKey(it)) return false
        }
        return true
    }

    fun getPropertiesFile(projectDir: File, propsFile: String): File {
        return if (File(propsFile).isAbsolute) {
            File(propsFile)
        } else {
            File(projectDir, propsFile)
        }
    }

    fun loadProperties(file: File): Properties {
        var isNew = false
        val props = Properties()
        file.apply {
            try {
                if (!exists() && createNewFile()) {
                    isNew = true
                }
            } catch (e: IOException) {
                throw GradleException("Unable to create: `$absoluteFile`", e)
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

        try {
            val max = 5
            val min = 3

            val parts = input.split(
                Regex("[\\Q${version.separator}${version.preReleasePrefix}${version.buildMetaPrefix}\\E]"),
                max
            )

            if (parts.size >= min) {
                version.major = parts[0].toInt()
                version.minor = parts[1].toInt()
                version.patch = parts[2].toInt()
                version.preRelease = ""
                version.buildMeta = ""

                if (parts.size > min) {
                    when (parts.size) {
                        max -> {
                            version.preRelease = parts[3]
                            version.buildMeta = parts[4]
                        }
                        4 -> {
                            if (input.endsWith(version.buildMetaPrefix + parts[3])) {
                                version.buildMeta = parts[3]
                            } else {
                                version.preRelease = parts[3]
                            }
                        }
                    }
                }
            } else {
                throw NumberFormatException("Not enough parts.")
            }
        } catch (e: NumberFormatException) {
            throw GradleException("Unable to parse version: \"$input\" (${e.message})", e)
        }

        return true
    }

    fun saveProperties(projectDir: File, config: SemverConfig, version: Version) {
        val propsFile = getPropertiesFile(projectDir, config.properties)
        SortedProperties().apply {
            try {
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

                    put(
                        config.buildMetaPrefixKey, version.buildMetaPrefix,
                        version.buildMetaPrefix != Version.DEFAULT_BUILDMETA_PREFIX ||
                            containsKey(config.buildMetaPrefixKey)
                    )
                    put(
                        config.preReleasePrefixKey, version.preReleasePrefix,
                        version.preReleasePrefix != Version.DEFAULT_PRERELEASE_PREFIX ||
                            containsKey(config.preReleasePrefixKey)
                    )
                    put(
                        config.separatorKey, version.separator,
                        version.separator != Version.DEFAULT_SEPARATOR ||
                            containsKey(config.separatorKey)
                    )

                    if (canWrite()) {
                        FileOutputStream(this).writer().use {
                            store(it, "Generated by the Semver Plugin for Gradle")
                        }
                    } else {
                        throw IOException("Can't write.")
                    }
                }
            } catch (e: IOException) {
                throw GradleException("Unable to write version to: `${propsFile.absoluteFile}`", e)
            }
        }
    }
}
