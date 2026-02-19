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
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.runtime)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
