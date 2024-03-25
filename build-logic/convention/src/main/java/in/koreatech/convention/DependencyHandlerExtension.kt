package `in`.koreatech.convention

import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.ksp(dependency: Any) {
    add("ksp", dependency)
}
fun DependencyHandler.kapt(dependency: Any) {
    add("kapt", dependency)
}

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: Any) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.api(dependency: Any) {
    add("api", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: Any) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.androidTestRuntimeOnly(dependency: Any) {
    add("androidTestRuntimeOnly", dependency)
}

fun DependencyHandler.kaptAndroidTest(dependency: Any) {
    add("kaptAndroidTest", dependency)
}

fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}

fun DependencyHandler.testRuntimeOnly(dependency: Any) {
    add("testRuntimeOnly", dependency)
}

fun DependencyHandler.kaptTest(dependency: Any) {
    add("kaptTest", dependency)
}

fun DependencyHandler.coreLibraryDesugaring(dependency: Any) {
    add("coreLibraryDesugaring", dependency)
}