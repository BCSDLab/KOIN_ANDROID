import `in`.koreatech.convention.implementation
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class FirebaseConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("firebase-appdistribution").get().get().pluginId)
                apply(libs.findPlugin("firebase-crashlytics").get().get().pluginId)
            }
            dependencies {
                implementation(platform(libs.findLibrary("firebase-bom").get()))
                implementation(libs.findBundle("firebase").get())
            }
        }
    }
}
