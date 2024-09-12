package `in`.koreatech.koin

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import `in`.koreatech.koin.core.toast.ToastUtil
import `in`.koreatech.koin.data.sharedpreference.RecentSearchSharedPreference
import `in`.koreatech.koin.data.sharedpreference.UserInfoSharedPreferencesHelper
import `in`.koreatech.koin.domain.repository.TokenRepository
import `in`.koreatech.koin.util.ExceptionHandlerUtil
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import javax.inject.Inject

@HiltAndroidApp
class KoinApplication : Application() {

    @Inject
    lateinit var tokenRepository: TokenRepository

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        init()
    }

    private fun init() {
        UserInfoSharedPreferencesHelper.getInstance().init(applicationContext)
        ToastUtil.getInstance().init(applicationContext)
        RecentSearchSharedPreference.getInstance().init(applicationContext)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandlerUtil(applicationContext))
    }
}