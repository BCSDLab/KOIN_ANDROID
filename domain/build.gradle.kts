plugins {
    alias(libs.plugins.koin.java)
    alias(libs.plugins.ktlint)
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
}
