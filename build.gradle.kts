// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    extra.apply {
        set("compileSdkVersion", 34)
        set("minSdkVersion", 24)
        set("targetSdkVersion", 34)
        set("versionName", "3.4.3")
        set("minVersionCode", 30403)
    }

    dependencies {
        classpath(libs.android.gradle.tool)
        classpath(libs.kotlin.gradle)
        classpath(libs.android.gradle.crashlytics)
        classpath(libs.firebase.appdistribution.gradle)
        classpath(libs.hilt.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.androidLibrary) apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
