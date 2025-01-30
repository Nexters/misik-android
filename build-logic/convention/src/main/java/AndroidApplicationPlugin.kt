import com.android.build.api.dsl.ApplicationExtension
import com.nexters.misik.convention.configureAndroidCompose
import com.nexters.misik.convention.configureKotlinAndroid
import com.nexters.misik.convention.extension.getLibrary
import com.nexters.misik.convention.extension.getVersion
import com.nexters.misik.convention.extension.implementation
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                configureAndroidCompose(this)
                with(defaultConfig) {
                    targetSdk = libs.getVersion("targetSdk").requiredVersion.toInt()
                    versionCode = libs.getVersion("versionCode").requiredVersion.toInt()
                    versionName = libs.getVersion("versionName").requiredVersion
                }
            }

            dependencies {
                implementation(libs.getLibrary("timber"))
            }
        }
    }
}
