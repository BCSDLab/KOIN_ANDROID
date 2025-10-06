import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import `in`.koreatech.convention.configureAndroidProject
import `in`.koreatech.convention.configureAndroidTest
import `in`.koreatech.convention.configureTest
import `in`.koreatech.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("ktlint").get().get().pluginId)
            }
            val extension = extensions.getByType<BaseAppModuleExtension>()
            extensions.configure<ApplicationExtension> {
                configureAndroidProject(extension)
                configureTest()
                configureAndroidTest()
            }
        }
    }
}
