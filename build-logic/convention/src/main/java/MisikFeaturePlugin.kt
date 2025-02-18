import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class MisikFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("misik.android.compose.library")
                apply("misik.android.hilt")
            }

            dependencies {
            }
        }
    }
}
