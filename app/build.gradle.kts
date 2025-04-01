plugins {
    id("plugins.android.application")
    id("plugins.android.compose")
}

dependencies {
    implementation(projects.core)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
}
