plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.misik.android.hilt)
}

android {
    namespace = "com.nexters.misik.data"
}

dependencies {
    implementation(projects.network)
    implementation(libs.timber)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(projects.domain)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
}

