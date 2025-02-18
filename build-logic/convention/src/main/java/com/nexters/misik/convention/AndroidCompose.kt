package com.nexters.misik.convention

import com.android.build.api.dsl.CommonExtension
import com.nexters.misik.convention.extension.debugImplementation
import com.nexters.misik.convention.extension.getBundle
import com.nexters.misik.convention.extension.getLibrary
import com.nexters.misik.convention.extension.implementation
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

    commonExtension.apply {
        buildFeatures {
            compose = true
        }
        extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
            enableStrongSkippingMode.set(true)
            includeSourceInformation.set(true)
        }
        dependencies {
            implementation(platform(libs.getLibrary("compose.bom")))
            implementation(libs.getBundle("compose"))
            debugImplementation(libs.getBundle("compose.debug"))
            implementation(libs.getLibrary("kotlinx.collections"))
        }
    }
}
