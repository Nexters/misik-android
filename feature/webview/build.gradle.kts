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
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.feature.preview)
    implementation(libs.gson)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.androidx.appcompat)
}
