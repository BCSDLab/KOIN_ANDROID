package `in`.koreatech.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal fun Project.configureAndroidTest(
) {
    dependencies {
        androidTestImplementation(libs.findLibrary("ext.junit").get())
        androidTestImplementation(libs.findLibrary("espresso.core").get())
    }
}
