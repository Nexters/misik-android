import org.gradle.api.Plugin
import org.gradle.api.Project

class MisikDataPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("misik.android.library")
                apply("misik.android.hilt")
            }
        }
    }
}
