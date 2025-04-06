plugins {
    id("plugins.android.library")
    id("plugins.dokka")
    alias(libs.plugins.vanniktech.publish)
}

android {
    namespace = "io.buszi.boomerang.core"
}

println(project.findProperty("signingInMemoryKeyId"))

dependencies {
    implementation(libs.androidx.lifecycle.runtime)
}
