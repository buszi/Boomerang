plugins {
    id("plugins.android.library")
    id("plugins.android.compose")
}

android {
    namespace = "io.buszi.boomerang.compose"
}

dependencies {
    implementation(projects.core)

    implementation(libs.compose.foundation)

    implementation(libs.androidx.lifecycle.compose)
}
