plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    compileOnly(libs.kotlin.gradle)
    compileOnly(libs.android.gradle.tool)
}

gradlePlugin {
    plugins {
        register("AndroidApplicationPlugin") {
            id = "in.koreatech.plugin.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("AndroidApplicationComposePlugin") {
            id = "in.koreatech.plugin.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
    }
}