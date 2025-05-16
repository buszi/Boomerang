plugins {
    id("plugins.multiplatform.library")
    id("plugins.multiplatform.compose")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

kotlin {
    androidLibrary {
        namespace = "io.buszi.boomerang.compose"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)

            implementation(compose.runtime)

            implementation(libs.androidx.lifecycle.compose)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
