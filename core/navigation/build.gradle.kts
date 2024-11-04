plugins {
    alias(libs.plugins.koin.library)
}

android {
    namespace = "in.koreatech.koin.core.navigation"
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
}