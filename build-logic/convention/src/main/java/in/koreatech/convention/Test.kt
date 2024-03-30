package `in`.koreatech.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies


internal fun Project.configureTest() {
    dependencies {
        testImplementation(libs.findLibrary("junit").get())
    }
}
