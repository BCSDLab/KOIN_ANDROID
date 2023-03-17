package gradleconfig

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*
import `in`.koreatech.buildsrc.util.implementation
import `in`.koreatech.buildsrc.util.libs

fun Project.configureNaverMap(commonExtension: CommonExtension<*, *, *, *>) {
    val naverMapsClientId: String = gradleLocalProperties(rootDir).getProperty("navermap_key")

    commonExtension.apply {
        defaultConfig {
            buildConfigField("String", "naverMapKey", "\"$naverMapsClientId\"")
            manifestPlaceholders["naverMapKey"] = naverMapsClientId
        }
    }

    dependencies {
        implementation(libs("naver.map.sdk"))
    }
}