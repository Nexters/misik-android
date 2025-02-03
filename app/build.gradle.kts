
plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
    alias(libs.plugins.misik.application)
    alias(libs.plugins.misik.android.hilt)

}

android {
    namespace = "com.nexters.misik"
}

dependencies {
    implementation(projects.feature.webview)
}
