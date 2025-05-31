package `in`.koreatech.koin.di.userAgent

import android.os.Build
import `in`.koreatech.koin.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAgentProvider @Inject constructor() {
    companion object {
        private const val APP_NAME = "koin"
        private const val LIBRARY_NAME = "OKHttp"
    }

    fun getUserAgent(): String {
        val appVersion = BuildConfig.VERSION_NAME
        val packageName = BuildConfig.APPLICATION_ID
        val buildNumber = BuildConfig.VERSION_CODE
        val osVersion = Build.VERSION.RELEASE
        val libraryVersion = BuildConfig.OKHTTP_VERSION

        return "$APP_NAME/$appVersion ($packageName; build:$buildNumber; Android $osVersion) $LIBRARY_NAME/$libraryVersion"
    }
}