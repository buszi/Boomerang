plugins {
    id("plugins.android.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

android {
    namespace = "io.buszi.boomerang.fragment.serialization.kotlinx"
}

dependencies {
    api(projects.core)
    api(projects.fragment)
    api(projects.serializationKotlinx)

    implementation(libs.androidx.fragment.core)
    implementation(libs.androidx.lifecycle.runtime)

    implementation(libs.kotlin.serialization.core)
}
