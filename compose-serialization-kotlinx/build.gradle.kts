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
            api(projects.core)
            api(projects.serializationKotlinx)
            api(projects.compose)

            implementation(compose.runtime)

            implementation(libs.androidx.lifecycle.compose)

            implementation(libs.kotlin.serialization.core)
        }
    }
}
