package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidOrbit(
    commonExtension: CommonExtension<*, *, *, *>
){
    dependencies {
        implementation(libs.findBundle("orbit").get())
    }
}