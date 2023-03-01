// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl("https://naver.jfrog.io/artifactory/maven/")
        }
        maven {
            setUrl( "https://jitpack.io")}
    }
}

plugins {
    alias(libs.plugins.firebase.appdistribution) apply false
    alias(libs.plugins.gms) apply false
}