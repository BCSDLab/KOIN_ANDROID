import dagger.hilt.android.plugin.HiltExtension
import `in`.koreatech.buildsrc.util.implementation
import `in`.koreatech.buildsrc.util.kapt
import `in`.koreatech.buildsrc.util.kaptAndroidTest
import `in`.koreatech.buildsrc.util.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

class AndroidHiltPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("dagger.hilt.android.plugin")
            apply("org.jetbrains.kotlin.kapt")
        }

        extensions.apply {
            configure<HiltExtension> {
                enableAggregatingTask = true
            }

            configure<KaptExtension> {
                correctErrorTypes = true
            }
        }

        dependencies {
            implementation(libs("hilt.android"))
            kapt(libs("hilt.compiler"))
        }
    }
}