# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/namhoonkim/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# Begin: Common Proguard rules
-dontwarn com.google.**
# End: Common Proguard rules

# 에러 발생시 라인 표시
-keepattributes SourceFile,LineNumberTable

# jdk 컴파일할 때 발생하는 오류 메시지 방지
-keepattributes EnclosingMethod

# 최적화 X
-dontoptimize

# 사용하지 않는 변수 남김
-dontshrink

#public 클래스 난독 시 메서드 호출 문제 발생 가능
-keep public class *

# Begin : lib
-keep class com.jakewharton.** { *; }
-keep class com.jpardogo.materialtabstrip.** { *; }
-keep interface com.jakewharton.** { *; }
-keep interface com.jpardogo.materialtabstrip.** { *; }
-keep class com.crashlytics.** { *; }
# End


# Begin : material, androidx
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
#End


# Begin: Proguard rules for retrofit2
-dontwarn java.lang.invoke.**
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8

# retrofit2 POJO model error
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
#-keep class com.bcsdlab.kap.community.networks.models.** { *; }
#-keepclassmembers class com.bcsdlab.kap.community.networks.models.** { *; }

# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
# End: Proguard rules for retrofit2

# Begin: Proguard rules for okhttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
# End: Proguard rules for okhttp3

# Begin: Proguard rules for Firebase
# Authentication
-keepattributes *Annotation*
# Realtime database
-keepattributes Signature
# End: Proguard rules for Firebase

# Proguard rules for BottomNavigationHelper
#-keepclassmembers class android.support.design.internal.BottomNavigationMenuView {
#    boolean mShiftingMode;
#}