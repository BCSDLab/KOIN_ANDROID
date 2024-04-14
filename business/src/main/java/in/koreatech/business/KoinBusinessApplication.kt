package `in`.koreatech.business

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import `in`.koreatech.koin.core.toast.ToastUtil

@HiltAndroidApp
class KoinBusinessApplication : Application(){
    companion object{
        lateinit var instance: KoinBusinessApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    private fun init() {
        ToastUtil.getInstance().init(applicationContext)
    }
}