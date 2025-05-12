plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("AndroidApplication") {
            id = "plugins.android.application"
            implementationClass = "io.buszi.boomerang.convention.AndroidApplicationPlugin"
        }
        register("AndroidLibrary") {
            id = "plugins.android.library"
            implementationClass = "io.buszi.boomerang.convention.AndroidLibraryPlugin"
        }
        register("KotlinMultiplatformLibrary") {
            id = "plugins.multiplatform.library"
            implementationClass = "io.buszi.boomerang.convention.KotlinMultiplatformLibraryPlugin"
        }
        register("AndroidCompose") {
            id = "plugins.android.compose"
            implementationClass = "io.buszi.boomerang.convention.AndroidComposePlugin"
        }
        register("ComposeMultiplatform") {
            id = "plugins.multiplatform.compose"
            implementationClass = "io.buszi.boomerang.convention.ComposeMultiplatformPlugin"
        }
        register("Dokka") {
            id = "plugins.dokka"
            implementationClass = "io.buszi.boomerang.convention.DokkaPlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.plgn.android.application)
    implementation(libs.plgn.android.library)
    implementation(libs.plgn.android.library.multiplatform)
    implementation(libs.plgn.kotlin.compose.compiler)
    implementation(libs.plgn.kotlin)
    implementation(libs.plgn.kotlin.multiplatform)
    implementation(libs.plgn.kotlin.dokka)
}
