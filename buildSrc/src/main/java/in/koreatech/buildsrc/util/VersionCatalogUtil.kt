package `in`.koreatech.buildsrc.util

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

fun Project.libs(library: String) =
    extensions.getByType<VersionCatalogsExtension>().named("libs").findLibrary(library).get()