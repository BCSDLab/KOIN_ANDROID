package gradleconfig

import com.android.build.api.dsl.CommonExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.getStorePassword(): String =
    gradleLocalProperties(rootDir).getProperty("koin_store_password")
