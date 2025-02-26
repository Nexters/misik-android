plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.misik.android.hilt)
}

android {
    namespace = "com.nexters.misik.core.data"
}

dependencies {
    implementation(projects.core.network)
    implementation(libs.timber)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(projects.core.domain)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
}