package `in`.koreatech.koin

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import `in`.koreatech.koin.core.analytics.EventLogger
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference
import `in`.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.domain.usecase.user.GetLoggerUserDataUseCase
import `in`.koreatech.koin.util.ExceptionHandlerUtil
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class KoinApplication : Application() {
    @Inject
    lateinit var tokenRepository: TokenRepository

    @Inject
    lateinit var crashlytics: FirebaseCrashlytics

    @Inject
    lateinit var getLoggerUserDataUseCase: GetLoggerUserDataUseCase

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext)
        ToastUtil.getInstance().init(applicationContext)
        RecentSearchSharedPreference.getInstance().init(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandlerUtil(applicationContext))
        initTimber()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
        EventLogger.init(getLoggerUserDataUseCase)
    }

    private fun initTimber() {
        if (BuildConfig.IS_DEBUG) {
            plantDebugTimberTree()
        } else {
            plantReleaseTimberTree()
        }
    }

    private fun plantDebugTimberTree() {
        Timber.plant(Timber.DebugTree())
    }

    private fun plantReleaseTimberTree() {
        val releaseTree =
            object : Timber.Tree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?,
                ) {
                    if (t != null) {
                        when (priority) {
                            Log.ERROR -> {
                                crashlytics.recordException(t)
                            }
                        }
                    }
                }
            }

        Timber.plant(releaseTree)
    }
}
