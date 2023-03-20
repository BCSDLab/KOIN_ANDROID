package `in`.koreatech.buildsrc.config

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import `in`.koreatech.buildsrc.util.implementation
import `in`.koreatech.buildsrc.util.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureRetrofit() {
    dependencies {
        implementation(
            libs("okhttp"),
            libs("okhttp.logging"),
            libs("retrofit"),
            libs("retrofit.gson.converter")
        )
    }
}