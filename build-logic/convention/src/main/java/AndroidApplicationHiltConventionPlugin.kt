import com.android.build.api.dsl.ApplicationExtension
import `in`.koreatech.convention.configureAndroidHilt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.dagger.hilt.android")
            }
            extensions.configure<ApplicationExtension> {
                configureAndroidHilt(this)
            }
        }
    }
}
