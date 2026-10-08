<img width="40%" src="https://github.com/user-attachments/assets/75742ede-7e33-435c-9330-c9b6ab232231"/>

# 코인 - 한기대 커뮤니티

[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg)](https://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/gradle-8.14.3-green.svg)](https://gradle.org/)
[![Android Gradle](https://img.shields.io/badge/AGP-8.13.0-green.svg)](https://gradle.org/)

[![minSdkVersion](https://img.shields.io/badge/minSdkVersion-28-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![compileSdkVersion](https://img.shields.io/badge/compileSdkVersion-36-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)
[![targetSdkVersion](https://img.shields.io/badge/targetSdkVersion-36-red)](https://developer.android.com/distribute/best-practices/develop/target-sdk)

> [!IMPORTANT]   
> 한국기술교육대학교의 학식, 주변 식당, 버스, 시간표, 공지사항 등 필수 정보를 제공하는   
> DAU 1,100, MAU 3,000명 이상 커뮤니티 서비스 앱으로, 재학생의 60% 이상이 사용하고 있습니다.

> 여러 직무의 팀원들과 각자 전문성을 살려 프로덕트를 개발하고 있습니다.   
> (BackEnd / FrontEnd / Android / iOS / Design / DA / PM / Security)

<p align="left">
  <a href="https://play.google.com/store/apps/details?id=in.koreatech.koin&hl=ko">
    <img src="https://img.shields.io/badge/Google%20Play-다운로드-414141?style=for-the-badge&logo=googleplay&logoColor=white&labelColor=01875F" alt="Google Play"/>
  </a>
  <a href="https://blog.bcsdlab.com/android">
    <img src="https://img.shields.io/badge/Tech%20Blog-BCSD%20블로그-414141?style=for-the-badge&logo=velog&logoColor=white&labelColor=7F52FF" alt="Tech Blog"/>
  </a>
</p>

## Tech Stack
| Category | Stack |                                                                                                                                                                                   
|:---:|:---|
| **Language** | ![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                   |
| **UI** | ![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white) ![XML](https://img.shields.io/badge/XML%20View-3DDC84?style=for-the-badge&logo=android&logoColor=white)                                                                                                                                                                                                                                                               |
| **Architecture** | ![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-1E88E5?style=for-the-badge) ![MVVM](https://img.shields.io/badge/MVVM-555555?style=for-the-badge) ![MVI](https://img.shields.io/badge/MVI-555555?style=for-the-badge) ![Orbit](https://img.shields.io/badge/Orbit%20MVI-6A1B9A?style=for-the-badge) ![Multi-Module](https://img.shields.io/badge/Multi--Module-0F9D58?style=for-the-badge&logo=gradle&logoColor=white)                                                          |
| **Jetpack** | ![Jetpack AAC](https://img.shields.io/badge/Jetpack%20AAC-3DDC84?style=for-the-badge&logo=android&logoColor=white) ![Room](https://img.shields.io/badge/Room-3DDC84?style=for-the-badge&logo=sqlite&logoColor=white)                                                                                                                                                                                                                                                                                    |
| **Async** | ![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white) ![Flow](https://img.shields.io/badge/Flow-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)                                                                                                                                                                                                                                                                                         |
| **Network** | ![Retrofit2](https://img.shields.io/badge/Retrofit2-48B983?style=for-the-badge&logo=square&logoColor=white) ![OkHttp3](https://img.shields.io/badge/OkHttp3-3E4348?style=for-the-badge&logo=square&logoColor=white)                                                                                                                                                                                                                                                                                     |
| **Serialization** | ![Gson](https://img.shields.io/badge/Gson-4285F4?style=for-the-badge&logo=google&logoColor=white) ![kotlinx.serialization](https://img.shields.io/badge/kotlinx.serialization-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)                                                                                                                                                                                                                                                                   |
| **DI** | ![Hilt](https://img.shields.io/badge/Hilt-2196F3?style=for-the-badge&logo=android&logoColor=white)                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Logging** | ![Timber](https://img.shields.io/badge/Timber-8D6E63?style=for-the-badge)                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **Third Party** | ![Kakao Share](https://img.shields.io/badge/Kakao%20Share-FFCD00?style=for-the-badge&logo=kakao&logoColor=000000) ![Naver Map](https://img.shields.io/badge/Naver%20Map-03C75A?style=for-the-badge&logo=naver&logoColor=white)                                                                                                                                                                                                                                                                          |
| **Firebase** | ![Google Analytics](https://img.shields.io/badge/Google%20Analytics-E37400?style=for-the-badge&logo=googleanalytics&logoColor=white) ![Crashlytics](https://img.shields.io/badge/Crashlytics-FFCA28?style=for-the-badge&logo=firebase&logoColor=000000) ![FCM](https://img.shields.io/badge/Cloud%20Messaging-FFCA28?style=for-the-badge&logo=firebase&logoColor=000000) ![App Distribution](https://img.shields.io/badge/App%20Distribution-FFCA28?style=for-the-badge&logo=firebase&logoColor=000000) |

## Module Structure
```text
KOIN_ANDROID/
├── koin/                   # KOIN 앱 모듈 (진입점)
├── domain/                 # Repository 인터페이스 · UseCase · 도메인 모델
├── data/                   # Repository 구현체 · API 서비스 · DTO
├── core/                   # 공통 인프라 모듈
│   ├── analytics/
│   ├── designsystem/
│   ├── navigation/
│   ├── network/
│   ├── notification/
│   ├── onboarding/
│   └── webapp/
├── feature/                # 기능(Feature) 모듈
│   ├── article/
│   ├── banner/
│   ├── bus/
│   ├── callvan/
│   ├── category/
│   ├── chat/
│   ├── club/
│   ├── department/
│   ├── dining/
│   ├── home/
│   ├── lostandfound/
│   ├── notification/
│   ├── profile/
│   ├── recruitment/
│   ├── setting/
│   ├── store/
│   ├── timetable/
│   └── user/
└── build-logic/            # Gradle Convention Plugin 모듈
```

## Feature
| 홈(home) | 카테고리(category) | 게시판(article) | 프로필(profile) |
|:--:|:--:|:--:|:--:|
| <img width="225" src="https://github.com/user-attachments/assets/e1fd4c45-207f-4c81-992a-08e8fbe967b0"/> | <img width="225" src="https://github.com/user-attachments/assets/c1c8ec35-6490-4f81-91c1-028ffca07238" /> | <img width="225" src="https://github.com/user-attachments/assets/c59d1653-189a-403d-8b95-9195396354ed" /> | <img width="225" src="https://github.com/user-attachments/assets/3c6d8728-9913-4759-8614-944e384a7644" /> |

<details>
<summary><b>상세 기능 화면</b></summary>
<br>

| 식단(dining) | 주변상점(store) | 버스(bus) | 분실물(lostandfound) |
|:--:|:--:|:--:|:--:|
| <img width="225" src="https://github.com/user-attachments/assets/b7571fab-b42e-42d1-92cf-2206173a3c2b" /> | <img width="225" src="https://github.com/user-attachments/assets/2187abb9-653e-419b-9a48-0a2fc428992d" />| <img width="225" src="https://github.com/user-attachments/assets/ab6db68b-14f5-47c5-8874-e7d50821b9b7" />| <img width="225" src="https://github.com/user-attachments/assets/1da3c313-e279-4f0a-9c5e-d52da14abdf7" />|
| 콜벤팟(callvan) | 팀원모집(recruitment) | 시간표(timetable) | 학교 부서정보(department) |
| <img width="225" src="https://github.com/user-attachments/assets/5aac4abf-106a-4c1d-9212-0f5e29a0d24c" /> | <img width="225" src="https://github.com/user-attachments/assets/76f9115a-93a4-478c-91d9-6a607aff3552" />| <img width="225" src="https://github.com/user-attachments/assets/2381d171-0ccc-4c5d-bc97-e0fc2780760c" />| <img width="225" src="https://github.com/user-attachments/assets/13052a11-ee8d-4587-a884-1333c110b417" />|
| 배너(banner) | 채팅(chat) | 알림(notification) |
| <img width="225" src="https://github.com/user-attachments/assets/32a91ebd-d5cf-4fc4-8541-27c0ba99b614" /> | <img width="225" src="https://github.com/user-attachments/assets/f0c59b22-6881-43fe-a5a9-f66ccec15e9d" />| <img width="225" src="https://github.com/user-attachments/assets/ec0d1dc2-de44-4001-830f-425bbd9336fa" />|
</details>

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
