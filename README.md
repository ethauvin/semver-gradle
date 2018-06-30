[![License (3-Clause BSD)](https://img.shields.io/badge/license-BSD%203--Clause-blue.svg?style=flat-square)](http://opensource.org/licenses/BSD-3-Clause) [![CircleCI](https://circleci.com/gh/ethauvin/semver-gradle/tree/master.svg?style=shield)](https://circleci.com/gh/ethauvin/semver-gradle/tree/master)

A [Semantic Version](https://semver.org) Plugin for [Gradle](https://gradle.org) that manages a project version via a properties file, and provide tasks to automatically increase major, minor and patch build numbers.

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

The `incrementMajor`, `incrementMinor` and `incrementPatch` are available to automatically increment their respective and reset lower counterpart version numbers.

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

### Version Properties

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

### Semver Task

The `semver` task is used to configure how the plugin will read/write the version properties file. It most cases it is not needed.

But, for example, if you wanted to save the version properties in a different file:

```gradle
semver {
    properties = "my.version" // read and save properties in "my.version"
}
```

or using different property keys for the version data:

```gradle
semver {
    majorKey = "major" // instead of the default version.major
    minorKey = "minor"
    patchKey = "patch"
    preReleaseKey = "release"
    buildMetaKey = "metadata"
}
```
which would match the data in `my.version`:

```ini
#my.version
major=1
minor=0
patch=0
release=beta
metadata=
```

The following task properties are available:

Properties            | Description                             | Default
:---------------------|:----------------------------------------|:-------------------------
`properties`          | The properties file.                    | `version.properties`
`majorKey`            | The major property key.                 | `version.major`
`minorKey`            | The minor property key.                 | `version.minor`
`patchKey`            | The patch property key.                 | `version.patch`
`preReleaseKey`       | The pre-release property key.           | `version.preRelease`
`preReleasePrefixKey` | The build pre-release prefix key.       | `version.preReleasePrefix`
`buildMetaKey`        | The build metadata property key.        | `version.buildMeta`
`buildMetaPrefixKey`  | The build metadata prefix property key. | `version.buildMetaPrefix`
`separatorKey`        | The separator property key.             | `version.separator`