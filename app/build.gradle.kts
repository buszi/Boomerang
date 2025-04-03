plugins {
    id("plugins.android.application")
    id("plugins.android.compose")
}

android {
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.compose)
    implementation(projects.fragment)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
}
