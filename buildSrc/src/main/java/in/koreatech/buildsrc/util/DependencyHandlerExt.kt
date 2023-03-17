package `in`.koreatech.buildsrc.util

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.debugImplementation(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("debugImplementation", dependency)
    }
}

fun DependencyHandler.api(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("api", dependency)
    }
}

fun DependencyHandler.debugApi(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("debugApi", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.testImplementation(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.kapt(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.kaptAndroidTest(vararg dependencies: Any) {
    dependencies.forEach { dependency ->
        add("kaptAndroidTest", dependency)
    }
}
