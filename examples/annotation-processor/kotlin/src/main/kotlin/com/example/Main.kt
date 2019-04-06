package com.example

import net.thauvin.erik.semver.Version
import java.io.File
import java.text.SimpleDateFormat

@Version(properties = "version.properties", type = "kt")
//@Version(
//    properties = "example.properties",
//    type = "kt",
//    keysPrefix = "example.",
//    preReleaseKey = "release",
//    buildMetaKey = "meta")
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val sdf = SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z")

            println("-----------------------------------------------------")

            println("  Version: ${GeneratedVersion.PROJECT} ${GeneratedVersion.VERSION}")

            println("    Built on:       " + sdf.format(GeneratedVersion.BUILDDATE))
            println("    Major:          ${GeneratedVersion.MAJOR}")
            println("    Minor:          ${GeneratedVersion.MINOR}")
            println("    Patch:          ${GeneratedVersion.PATCH}")
            println("    PreRelease:     ${GeneratedVersion.PRERELEASE}")
            println("    BuildMetaData:  ${GeneratedVersion.BUILDMETA}")

            println("-----------------------------------------------------")

            if (args.size == 1) {
                File(args[0]).apply {
                    if (exists()) {
                        println("> cat $name")
                        forEachLine {
                            println(it)
                        }
                    }
                }
            }
        }
    }
}
