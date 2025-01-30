import com.nexters.misik.convention.Const
import com.nexters.misik.convention.extension.getLibrary
import com.nexters.misik.convention.extension.getVersion
import com.nexters.misik.convention.extension.implementation
import com.nexters.misik.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

class JavaLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
                apply("org.jetbrains.kotlin.jvm")
            }

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = Const.JAVA_VERSION
                targetCompatibility = Const.JAVA_VERSION
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(libs.getVersion("jdkVersion").requiredVersion.toInt())
            }

            dependencies {
                implementation(libs.getLibrary("javax.inject"))
            }
        }
    }
}
