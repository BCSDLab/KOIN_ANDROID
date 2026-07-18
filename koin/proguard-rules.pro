# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/namhoonkim/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
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

# Begin : lib
-keep class com.jakewharton.** { *; }
-keep interface com.jakewharton.** { *; }
# End


# Begin : material, androidx
-dontwarn com.google.android.material.**
-dontwarn androidx.**
#End

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
-keep class com.kakao.sdk.**.model.* { <fields>; }
#}

-dontwarn org.jspecify.annotations.NullMarked
-dontwarn top.defaults.checkerboarddrawable.CheckerboardDrawable