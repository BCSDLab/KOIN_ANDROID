import `in`.koreatech.convention.implementation
import `in`.koreatech.convention.libs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.api.Plugin
import org.gradle.api.Project

class OssLicensePlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.android.gms.oss-licenses-plugin")
            }

            dependencies {
                implementation(libs.findLibrary("oss-licenses").get())
            }
        }
    }
}