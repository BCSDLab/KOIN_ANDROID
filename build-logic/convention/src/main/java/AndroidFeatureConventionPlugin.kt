import com.android.build.api.dsl.LibraryExtension
import `in`.koreatech.convention.configureAndroidCompose
import `in`.koreatech.convention.configureAndroidProject
import `in`.koreatech.convention.configureAndroidTest
import `in`.koreatech.convention.configureTest
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("androidLibrary").get().get().pluginId)
                apply(libs.findPlugin("kotlin-android").get().get().pluginId)
                apply(libs.findPlugin("kotlin-parcelize").get().get().pluginId)
                apply(libs.findPlugin("ksp").get().get().pluginId)
                apply(libs.findPlugin("ktlint").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidProject(this)
                configureAndroidCompose(this)
                configureTest()
                configureAndroidTest()
            }
        }
    }
}