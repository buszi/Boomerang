plugins {
    id("plugins.android.library")
    id("plugins.dokka")
}

android {
    namespace = "io.buszi.boomerang.core"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime)
}
