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

    fun loadProperty(props: Properties, key: String, default: String): String {
        return System.getProperty(key, if (props.isNotEmpty()) props.getProperty(key, default) else default)
    }

    fun loadVersion(config: SemverConfig, version: Version, props: Properties) {
        props.apply {
            if (!parseSemVer(System.getProperty(config.semverKey), version)) {
                version.major = loadProperty(this, config.majorKey, Version.DEFAULT_MAJOR)
                version.minor = loadProperty(this, config.minorKey, Version.DEFAULT_MINOR)
                version.patch = loadProperty(this, config.patchKey, Version.DEFAULT_PATCH)
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
        if (input == null) return false

        var semver = StringBuilder(input)
        var start = semver.indexOf(version.separator)
        var minor = ""
        var major = ""
        var patch = ""
        var preRelease = ""
        var buildMeta = ""

        // major
        if (start != -1) {
            major = semver.substring(0, start)
            semver.delete(0, start + major.length)
            start = semver.indexOf(version.separator)
            // minor
            if (start != -1) {
                minor = semver.substring(0, start)
                semver = semver.delete(0, start + minor.length)
                start = semver.indexOf(version.preReleasePrefix)
                // patch
                if (start != -1) {
                    patch = semver.substring(0, start)
                    semver.delete(0, start + minor.length)
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
                    patch = semver.toString()
                    semver.clear()
                }
            }
        }

        if (semver.isNotEmpty()) throw GradleException("Unable to parse version: `$input`.")

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
                put(config.majorKey, version.major)
                put(config.minorKey, version.minor)
                put(config.patchKey, version.patch)
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