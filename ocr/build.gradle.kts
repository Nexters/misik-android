import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.misik.android.library)
    alias(libs.plugins.misik.android.hilt)
    alias(libs.plugins.misik.plugin.build.config)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.nexters.misik.ocr"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
        manifestPlaceholders["mlkit_vision_dependencies"] =
            "ocr,ocr_korean"
        resValue(
            "string",
            "cloud_vision_api_key",
            localProperties.getProperty("CLOUD_VISION_API_KEY", ""),
        )
        buildConfigField(
            "String",
            "CLOUD_VISION_API_KEY",
            "\"${localProperties.getProperty("CLOUD_VISION_API_KEY", "")}\"",
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro",
//            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(projects.domain)
    implementation(libs.timber)
    implementation(libs.gson)

    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.play.services.mlkit.text.recognition.korean)

    implementation(libs.mlkit.text.recognition)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}