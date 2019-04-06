package com.example;

import net.thauvin.erik.semver.Version;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import java.text.SimpleDateFormat;

@Version(properties = "version.properties")
//@Version(
//    properties = "example.properties",
//    keysPrefix = "example.",
//    preReleaseKey = "release",
//    buildMetaKey = "meta")
public class Example {
    public static void main(String... args) throws IOException {
        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss z");

        System.out.println("-----------------------------------------------------");

        System.out.println("  Version: " + GeneratedVersion.PROJECT + ' ' + GeneratedVersion.VERSION);

        System.out.println("    Built on:       " + sdf.format(GeneratedVersion.BUILDDATE));
        System.out.println("    Major:          " + GeneratedVersion.MAJOR);
        System.out.println("    Minor:          " + GeneratedVersion.MINOR);
        System.out.println("    Patch:          " + GeneratedVersion.PATCH);
        System.out.println("    PreRelease:     " + GeneratedVersion.PRERELEASE);
        System.out.println("    BuildMetaData:  " + GeneratedVersion.BUILDMETA);

        System.out.println("-----------------------------------------------------");

        if (args.length == 1) {
            final Path path = Paths.get(args[0]);
            if (Files.exists(path)) {
                final List<String> content = Files.readAllLines(path);
                System.out.println("> cat " + path.getFileName());
                for (final String line : content) {
                    System.out.println(line);
                }
            }
        }
    }
}
