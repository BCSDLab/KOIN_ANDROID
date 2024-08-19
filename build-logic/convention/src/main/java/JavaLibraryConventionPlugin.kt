import `in`.koreatech.convention.configureKotlinJvm
import `in`.koreatech.convention.implementation
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JavaLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("java-library")
            }
            dependencies {
                implementation(libs.findLibrary("javax-inject").get())
            }
            configureKotlinJvm()
        }
    }
}