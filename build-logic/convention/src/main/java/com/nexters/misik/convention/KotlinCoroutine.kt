package com.nexters.misik.convention

import com.android.build.api.dsl.CommonExtension
import com.nexters.misik.convention.extension.getBundle
import com.nexters.misik.convention.extension.implementation
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureKotlinCoroutine(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        dependencies {
            implementation(libs.getBundle("coroutine"))
        }
    }
}
