import `in`.koreatech.convention.implementation
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.extra

internal class FirebaseConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.firebase.crashlytics")
                apply("com.google.firebase.appdistribution")
            }
            dependencies {
                implementation(platform(libs.findLibrary("firebase-bom").get()))
                implementation(libs.findBundle("firebase").get())
            }
        }
    }
}