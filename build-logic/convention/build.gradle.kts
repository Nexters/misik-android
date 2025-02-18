plugins {
    `kotlin-dsl`
}

group = "com.nexters.misik.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.extension)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "misik.android.application"
            implementationClass = "AndroidApplicationPlugin"
        }

        register("misikFeature") {
            id = "misik.feature"
            implementationClass = "MisikFeaturePlugin"
        }

        register("misikData") {
            id = "misik.data"
            implementationClass = "MisikDataPlugin"
        }

        register("androidLibrary") {
            id = "misik.android.library"
            implementationClass = "AndroidLibraryPlugin"
        }

        register("androidComposeLibrary") {
            id = "misik.android.compose.library"
            implementationClass = "AndroidComposeLibraryPlugin"
        }

        register("androidHilt") {
            id = "misik.android.hilt"
            implementationClass = "HiltPlugin"
        }

        register("javaLibrary") {
            id = "misik.java.library"
            implementationClass = "JavaLibraryPlugin"
        }

        register("buildConfig") {
            id = "misik.plugin.build.config"
            implementationClass = "BuildConfigPlugin"
        }

        register("androidTest") {
            id = "misik.plugin.android.test"
            implementationClass = "AndroidTestPlugin"
        }

        register("androidFirebase") {
            id = libs.plugins.misik.android.application.firebase.get().pluginId
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }
    }
}
