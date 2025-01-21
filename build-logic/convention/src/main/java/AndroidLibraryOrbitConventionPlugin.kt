import com.android.build.api.dsl.LibraryExtension
import `in`.koreatech.convention.configureAndroidOrbit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryOrbitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<LibraryExtension> {
                configureAndroidOrbit(this)
            }
        }
    }
}
