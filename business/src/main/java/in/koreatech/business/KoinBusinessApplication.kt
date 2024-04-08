package `in`.koreatech.business

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KoinBusinessApplication : Application(){
    companion object{
        lateinit var instance: KoinBusinessApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}