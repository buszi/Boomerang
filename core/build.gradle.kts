plugins {
    id("plugins.multiplatform.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

kotlin {
    androidLibrary {
        namespace = "io.buszi.boomerang.core"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime)
        }
    }
}
