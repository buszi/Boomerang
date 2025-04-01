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
        register("AndroidCompose") {
            id = "plugins.android.compose"
            implementationClass = "io.buszi.boomerang.convention.AndroidComposePlugin"
        }
    }
}

dependencies {
    implementation(gradleApi())
    implementation(libs.plgn.andrid.application)
    implementation(libs.plgn.andrid.library)
    implementation(libs.plgn.kotlin.compose.compiler)
    implementation(libs.plgn.kotlin)
}
