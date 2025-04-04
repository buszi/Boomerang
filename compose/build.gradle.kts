plugins {
    id("plugins.android.library")
    id("plugins.android.compose")
    id("plugins.dokka")
}

android {
    namespace = "io.buszi.boomerang.compose"
}

dependencies {
    implementation(projects.core)

    implementation(libs.compose.foundation)

    implementation(libs.androidx.lifecycle.compose)
    implementation(libs.androidx.activity.compose)
}
