import com.android.build.gradle.LibraryExtension
import com.nexters.misik.convention.configureKotlinAndroid
import com.nexters.misik.convention.configureKotlinCoroutine
import com.nexters.misik.convention.extension.getLibrary
import com.nexters.misik.convention.extension.implementation
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureKotlinCoroutine(this)
            }

            dependencies {
                implementation(libs.getLibrary("timber"))
            }
        }
    }
}
