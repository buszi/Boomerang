plugins {
    id("plugins.android.application")
    id("plugins.android.compose")
}

dependencies {
    implementation(projects.core)
    implementation(projects.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
}
