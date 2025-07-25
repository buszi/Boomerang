plugins {
    id("plugins.multiplatform.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "io.buszi.boomerang.serialization.kotlinx"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core)

            implementation(libs.kotlin.serialization.core)
        }
        
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}
