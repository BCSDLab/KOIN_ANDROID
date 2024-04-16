package `in`.koreatech.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidHilt(
    commonExtension: CommonExtension<*, *, *, *>,
){
    dependencies {
        implementation(libs.findBundle("hilt").get())
        kapt(libs.findLibrary("hilt-compiler").get())
    }
}