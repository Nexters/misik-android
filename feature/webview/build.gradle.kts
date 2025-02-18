plugins {
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.feature)
    alias(libs.plugins.misik.android.hilt)
    alias(libs.plugins.misik.plugin.build.config)
}

android {
    namespace = "com.nexters.misik.feature.webview"

}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.data)
    implementation(projects.domain)
    implementation(projects.network)
    implementation(projects.feature.preview)
    implementation(libs.gson)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.androidx.appcompat)
}
