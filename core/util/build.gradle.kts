import `in`.koreatech.buildsrc.util.implementation

plugins {
    id("koin.android.library")
}

android {
    namespace = "in.koreatech.core.util"
}

dependencies {
    implementation(libs.androidx.appcompat)
}