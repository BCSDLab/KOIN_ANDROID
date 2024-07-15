plugins {
    alias(libs.plugins.koin.java)
    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.napier)
}
