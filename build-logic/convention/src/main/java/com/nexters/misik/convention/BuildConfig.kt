package com.nexters.misik.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.nexters.misik.convention.extension.getVersion
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Project

internal fun Project.configureBuildConfig(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        defaultConfig {
            // BASE_URL
            buildConfigField(
                "String",
                "BASE_URL",
                gradleLocalProperties(rootDir, providers).getProperty("base.url"),
            )

            // VERSION_CODE, VERSION_NAME
            val versionCode = libs.getVersion("versionCode").requiredVersion.toInt()
            val versionName = libs.getVersion("versionName").requiredVersion

            buildConfigField(
                "int",
                "VERSION_CODE",
                versionCode.toString(),
            )

            buildConfigField(
                "String",
                "VERSION_NAME",
                "\"$versionName\"",
            )
        }

        buildFeatures {
            buildConfig = true
        }
    }
}
