plugins {
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.feature)
    alias(libs.plugins.misik.android.hilt)
}

android {
    namespace = "com.nexters.misik.feature.webview"
}

dependencies {
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.network)
    implementation(projects.feature.preview)
    implementation(libs.gson)
}
