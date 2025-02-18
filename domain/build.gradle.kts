plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.misik.android.hilt)
}

android {
    namespace = "com.nexters.misik.domain"
}

dependencies {
    implementation(libs.timber)
}

