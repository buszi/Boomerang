plugins {
    id("plugins.multiplatform.library")
    id("plugins.multiplatform.compose")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

kotlin {
    androidLibrary {
        namespace = "io.buszi.boomerang.compose.serialization.kotlinx"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.serializationKotlinx)
            implementation(projects.compose)

            implementation(compose.runtime)

            implementation(libs.androidx.lifecycle.compose)

            implementation(libs.kotlin.serialization.core)
        }
    }
}
