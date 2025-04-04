plugins {
    id("plugins.android.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

android {
    namespace = "io.buszi.boomerang.core"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime)
}
