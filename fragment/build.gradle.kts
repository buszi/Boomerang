plugins {
    id("plugins.android.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

android {
    namespace = "io.buszi.boomerang.fragment"
}

dependencies {
    api(projects.core)

    implementation(libs.androidx.fragment.core)
    implementation(libs.androidx.lifecycle.runtime)
}
