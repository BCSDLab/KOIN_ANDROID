// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    extra.apply {
        set("versionName", "4.6.1")
        set("versionCode", 40601)
        // 코인 버전 관리

        set("versionBusinessName", "1.0.1")
        set("versionBusinessCode", 1000002)
        //코안 사장님 버전 관리
    }

    dependencies {
        classpath(libs.android.gradle.tool)
        classpath(libs.kotlin.gradle)
        classpath(libs.firebase.crashlytics.gradle)
        classpath(libs.firebase.appdistribution.gradle)
        classpath(libs.oss.licenses.plugin)
        classpath(libs.hilt.gradle.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.kover)
    alias(libs.plugins.sonarqube)
}

kover {
    merge {
        allProjects()
    }
}

sonar {
    properties {
        property("sonar.projectKey", "BCSDLab_KOIN_ANDROID")
        property("sonar.organization", "bcsdlab")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/kover/report.xml")
        property("sonar.androidLint.reportPaths", "**/build/reports/lint-results*.xml")
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("**/*.kts")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.named<Delete>("clean") {
    delete(rootProject.buildDir)
}
