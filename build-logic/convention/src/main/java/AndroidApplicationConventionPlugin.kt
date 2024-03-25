import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import `in`.koreatech.convention.configureAndroidProject
import `in`.koreatech.convention.configureAndroidTest
import `in`.koreatech.convention.configureTest
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val extension = extensions.getByType<BaseAppModuleExtension>()
            extensions.configure<ApplicationExtension> {
                configureAndroidProject(extension)
                configureTest()
                configureAndroidTest()
            }
        }
    }
}
