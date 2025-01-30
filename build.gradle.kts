plugins {
    alias(libs.plugins.ktlint)
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.compose.compiler) apply false
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.50.0").userData(
            mapOf(
                "indent_size" to "4",
                "continuation_indent_size" to "4",
            ),
        )
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint().userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports, import-ordering, trailing-comma",
            ),
        )
    }
}

detekt {
    config.from("${projectDir}/config/detekt/detekt-config.yml")
    buildUponDefaultConfig = true
    debug = true
}
