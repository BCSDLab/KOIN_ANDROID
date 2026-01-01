<img src="koin.png" width=100px height=100px >

# 코인 - 한기대 커뮤니티

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg)](https://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.14.3-green.svg)](https://gradle.org/)
[![Android Gradle](https://img.shields.io/badge/AGP-8.13.0-green.svg)](https://gradle.org/)

[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-28-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![compileSdkVersion](https://img.shields.io/badge/compileSdkVersion-35-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-35-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)

코인은 한국기술교육대학교 학생들을 위하여 제공하는 커뮤니티 플랫폼 서비스입니다.

[코인 - 한기대 커뮤니티 : Google Play Store 바로가기](https://play.google.com/store/apps/details?id=in.koreatech.koin&hl=ko)

[✨ BCSD 블로그와 함께 코인 프로젝트 훔쳐보기 ✨](https://blog.bcsdlab.com/introduce)

## 코인 사장님
`코인 - 한기대 커뮤니티` 앱과는 별도로 사장님들에게 직접 가게를 등록할 수 있도록 코인 사장님 앱을 제공하고 있습니다.<br>

[코인 사장님 : Google Play Store 바로가기](https://play.google.com/store/apps/details?id=in.koreatech.business&hl=ko)

## Tech Stack
- Java & Kotlin
- XML & Compose
- Jetpack AAC
- Coroutine Flow
- Multi-Module
- MVVM & MVI
- Orbit
- Retrofit2 & OkHttp3
- Gson & kotlinx.serialization
- Hilt
- Timber
- Kakao share
- Naver Map
- Google Analytics
- Firebase Crashlytics
- Firebase Cloud Message
- Firebase App Distribution



## Git Branch Strategy


```mermaid
---
title: KOIN Git Flow
---

%%{init: { 'logLevel': 'debug', 'theme': 'base', 'gitGraph': {'showBranches': true, 'mainBranchName': 'production'}} }%%
      gitGraph
        commit tag: "v1.0.0"
        branch hotfix/A
        checkout production
        branch develop
        checkout develop
        commit
        branch feature/A
        checkout feature/A
        checkout production
        checkout hotfix/A
        commit
        checkout develop
        checkout feature/A
        commit
        checkout production
        merge hotfix/A tag: "v1.0.1"
        checkout feature/A
        commit
        checkout develop
        branch feature/B
        commit
        checkout develop
        merge hotfix/A
        checkout feature/B
        commit
        checkout feature/A
        commit
        checkout develop
        merge feature/A
        branch release/v1.1.0
        checkout develop
        merge feature/B
        branch release/v1.1.0B
        checkout release/v1.1.0
        commit
        commit
        checkout release/v1.1.0B
        commit
        commit
        checkout production
        merge release/v1.1.0 tag: "v1.1.0"
        merge release/v1.1.0B tag: "v1.1.0B"
        checkout release/v1.1.0
        checkout develop
        merge release/v1.1.0
        merge release/v1.1.0B
```