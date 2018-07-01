[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause) [![CircleCI](https://circleci.com/gh/ethauvin/semver-gradle/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/semver-gradle/tree/master)

# Semantic Version Plugin for Gradle

A [Semantic Version](https://semver.org) Plugin for [Gradle](https://gradle.org) that manages a project version via a properties file, and provide tasks to __automatically increment__ major, minor and patch build numbers.

The plugin can be used in conjunction with the [Semantic Version Annotation Processor](https://github.com/ethauvin/semver).

## Using the plugin

The plugin is published to the Plugin Portal; see instructions there: [net.thauvin.erik.gradle.semver](https://plugins.gradle.org/plugin/net.thauvin.erik.gradle.semver)

## Version Properties File

By default, a `version.properties` file will be created when Gradle is run:

```ini
#version.properties
version.major=1
version.minor=0
version.patch=0
version.preRelease=
version.buildMeta=
```

To change the version of your project, remove the version from your `build.gradle` and simply edit your the version properties file to match your version number.

If you need to change some of the property file or name of the properties key to match your own build environment. Please see the [Configuration](#configuration) section.

## Increment Version Tasks

The `incrementMajor`, `incrementMinor` and `incrementPatch` tasks are available to automatically increment their respective and reset lower counterpart version numbers.

- `incrementMajor` will increment the `major` and set the `minor` and `patch` versions to `0`.
- `incrementMinor` will increment the `minor` and set the path version to `0`.

#### Examples

```sh
./gradlew incrementPatch ...
```

or in your `gradle.build` file:

```gradle
someTask {
    dependsOn(incrementPatch)
    ...
}
```

## Configuration

### Version File Properties

The following default properties are recognized:

Property                   | Description                 | Default
:--------------------------|:----------------------------|:---
`version.major`            | The major version.          | `1`
`version.minor`            | The minor version.          | `0`
`version.patch`            | The patch version.          | `0`
`version.preRelease`       | The pre-release version     |
`version.buildMeta `       | The build metatdata version |
`version.preReleasePrefix` | The pre-release prefix      | `-`
`version.buildMetaPrefix ` | The build metadata prefix   | `+`
`version.separator`        | The version separator.      | `.`

The version number is built as follows:


`version.major` `version.separtor` `version.minor` `version.separator` `version.preReleasePrefix` `version.preRelease` `version.buildMetaPrefix` `version.buildMeta`

for example:

```ini
#version.properties
version.major=1
version.minor=0
version.patch=0
version.preRelease=beta
version.buildMeta=exp.sha.5114f85
```

`projet.version` will be `1.0.0-beta+exp.sha.5114f85` in Gradle.


### Semver

In a Gradle build file, the `semver` block is used to configure how the plugin will read/write the version properties file. In most cases it is not needed.

But, for example, if you wanted to save the version properties in a different file:

```gradle
semver {
    properties = "my.version" // read and save properties in "my.version"
}
```

or using different property keys for the version data:

```gradle
semver {
    keysPrefix = "" // no prefix
    majorKey = "major"
    minorKey = "minor"
    patchKey = "patch"
    preReleaseKey = "prerelease"
    buildMetaKey = "metadata"
}
```
which would match the data in `my.version`:

```ini
#my.version
major=1
minor=0
patch=0
prerelease=beta
metadata=
```

The following `semver` properties are available:

Property              | Description                             | Default
:---------------------|:----------------------------------------|:------------------------
`properties`          | The properties file.                    | `version.properties`
`majorKey`            | The major property key.                 | `major`
`minorKey`            | The minor property key.                 | `minor`
`patchKey`            | The patch property key.                 | `patch`
`preReleaseKey`       | The pre-release property key.           | `preRelease`
`preReleasePrefixKey` | The build pre-release prefix key.       | `preReleasePrefix`
`buildMetaKey`        | The build metadata property key.        | `buildMeta`
`buildMetaPrefixKey`  | The build metadata prefix property key. | `buildMetaPrefix`
`separatorKey`        | The separator property key.             | `separator`
`keysPrefix`          | The prefix for all property keys.       | `version.`

In order to quickly support multiple projects. The `keysPrefix` property is available to set all properties keys prefixes at once:

```gradle
semver {
    properties = "test.properties"
    keysPrefix = "test."
}
```

```ini
#test.properties
test.major=1
test.minor=0
test.patch=0
test.preRelease=
test.buildMeta=
```

## Source Code Generation

If you'd like to incorporate the version number data into your source code, please have a look at the [Semantic Version Annotation Processor](https://github.com/ethauvin/semver).