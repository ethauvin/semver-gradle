plugins {
  id("com.gradle.develocity") version "4.0.1"
}

develocity {
    buildScan {
      link("GitHub", "https://github.com/ethauvin/semver/tree/master")
      if (!System.getenv("CI").isNullOrEmpty()) {
          uploadInBackground.set(false)
          publishing.onlyIf { true }
          tag("CI")
      }
      termsOfUseUrl.set("https://gradle.com/help/legal-terms-of-use")
      termsOfUseAgree.set("yes")
    }
}

rootProject.name = "semver"
