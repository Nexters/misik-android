plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.android.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.misik.plugin.build.config)

}

android {
    namespace = "com.hyeseon.misik.network"
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.timber)
}
