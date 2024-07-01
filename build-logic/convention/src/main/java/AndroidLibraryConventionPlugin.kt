import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import `in`.koreatech.convention.configureAndroidLibrary
import `in`.koreatech.convention.configureAndroidTest
import `in`.koreatech.convention.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("kotlin-kapt")
                apply("com.android.library")
                apply("kotlin-android")
                apply("org.jetbrains.kotlin.android")
            }
            extensions.configure<LibraryExtension> {
                configureAndroidLibrary(this)
                configureTest()
                configureAndroidTest()
            }
        }
    }

}