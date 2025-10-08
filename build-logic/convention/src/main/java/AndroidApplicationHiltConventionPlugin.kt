import `in`.koreatech.convention.implementation
import `in`.koreatech.convention.ksp
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplicationHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("hilt").get().get().pluginId)
            }
            dependencies {
                implementation(libs.findBundle("hilt").get())
                ksp(libs.findLibrary("hilt-compiler").get())
            }
        }
    }
}
