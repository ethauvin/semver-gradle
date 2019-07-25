/*
 * SemverPlugin.kt
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
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.util.GradleVersion

class SemverPlugin : Plugin<Project> {
    private val simpleName = SemverPlugin::class.simpleName
    private var version = Version()
    private lateinit var config: SemverConfig

    override fun apply(project: Project) {
        if (GradleVersion.current() < GradleVersion.version("4.8.1")) {
            throw GradleException("The $simpleName plugin requires Gradle version 4.8.1 or greater.")
        }
        config = project.extensions.create("semver", SemverConfig::class.java, version)
        project.afterEvaluate(this::afterEvaluate)

        project.tasks.apply {
            create("incrementMajor", SemverIncrementTask::class.java, config, version, SemverConfig.DEFAULT_MAJOR_KEY)
            create("incrementMinor", SemverIncrementTask::class.java, config, version, SemverConfig.DEFAULT_MINOR_KEY)
            create("incrementPatch", SemverIncrementTask::class.java, config, version, SemverConfig.DEFAULT_PATCH_KEY)
            create("incrementBuildMeta", SemverIncrementBuildMetaTask::class.java, config, version)
        }
    }

    private fun afterEvaluate(project: Project) {
        val propsFile = Utils.getPropertiesFile(project.projectDir, config.properties)

        if (project.version != "unspecified") {
            project.logger.warn(
                "Please specify the version in ${propsFile.name} and remove it from ${project.buildFile.name}"
            )
        }

        propsFile.apply {
            val isNew = !exists()

            project.logger.info(
                "[$simpleName] Attempting to read properties from: `$absoluteFile`. [exists: $isNew, isFile: $isFile, canRead: ${canRead()}]"
            )

            val props = Utils.loadProperties(this)
            val requiredProps = setOf(
                config.semverKey, config.majorKey, config.minorKey, config.patchKey,
                config.preReleaseKey, config.buildMetaKey
            )
            val hasReqProps = !isNew && props.stringPropertyNames().containsAll(requiredProps) &&
                Utils.isNotSystemProperty(requiredProps)

            Utils.loadVersion(config, version, props)

            project.tasks.withType(SemverIncrementBuildMetaTask::class.java) {
                buildMeta = version.buildMeta
            }

            project.version = version.semver
            project.logger.info("[$simpleName] Project version set to: ${project.version}")

            if (!hasReqProps || !isFile) {
                project.logger.info("[$simpleName] Saving version properties to `$absoluteFile`.")
                Utils.saveProperties(project.projectDir, config, version)
            }
        }
    }
}
