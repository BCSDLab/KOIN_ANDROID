import com.android.build.api.dsl.ApplicationExtension
import `in`.koreatech.convention.configureAndroidOrbit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationOrbitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                configureAndroidOrbit(this)
            }
        }
    }
}
