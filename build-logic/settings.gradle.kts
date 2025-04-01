pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// Use the same version catalogue as the main project
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
include(":convention")
