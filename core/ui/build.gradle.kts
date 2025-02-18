plugins {
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.feature)
}

android {
    namespace = "com.nexters.misik.core.ui"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}