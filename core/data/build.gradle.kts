import `in`.koreatech.buildsrc.config.configureRetrofit

plugins {
    id("koin.android.library")
    id("koin.android.hilt")
}

android {
    namespace = "in.koreatech.core.data"

    configureRetrofit()
}

dependencies {
    implementation(project(":core:security"))
}