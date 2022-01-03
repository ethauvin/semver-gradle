plugins {
  id("com.gradle.enterprise").version("3.6.3")
}

gradleEnterprise {
    buildScan {
      link("GitHub", "https://github.com/ethauvin/semver/tree/master")
      if (!System.getenv("CI").isNullOrEmpty()) {
          isUploadInBackground = false
          publishOnFailure()
          tag("CI")
      }
      termsOfServiceUrl = "https://gradle.com/terms-of-service"
      termsOfServiceAgree = "yes"
    }
}

rootProject.name = "semver"
