package com.example;

import net.thauvin.erik.semver.Version;

import java.text.SimpleDateFormat;

@Version(properties = "version.properties")
//@Version(
//    properties = "example.properties",
//    keysPrefix = "example.",
//    preReleaseKey = "release",
//    buildMetaKey = "meta")
public class Example {
    public static void main(final String... args) {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");

        System.out.println("-----------------------------------------------------");

        System.out.println("  Version:" + GeneratedVersion.PROJECT + ' ' + GeneratedVersion.VERSION);

        System.out.println("    Built on:       " + sdf.format(GeneratedVersion.BUILDDATE));
        System.out.println("    Major:          " + GeneratedVersion.MAJOR);
        System.out.println("    Minor:          " + GeneratedVersion.MINOR);
        System.out.println("    Patch:          " + GeneratedVersion.PATCH);
        System.out.println("    PreRelease:     " + GeneratedVersion.PRERELEASE);
        System.out.println("    BuildMetaData:  " + GeneratedVersion.BUILDMETA);

        System.out.println("-----------------------------------------------------");
    }
}
