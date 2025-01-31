plugins {
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.feature)
}

android {
    namespace = "com.nexters.misik.feature.webview"
}

dependencies {
    implementation(projects.domain)
}
