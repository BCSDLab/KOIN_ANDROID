package `in`.koreatech.koin.ui.navigation

import android.os.Bundle
import android.os.SystemClock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class KoinNavigationDrawerTimeActivity : KoinNavigationDrawerActivity() {

    private var startTime: Long = 0
    protected var elapsedTime: Long = 0
        get() = field + SystemClock.elapsedRealtime() - startTime

    override fun onPause() {
        super.onPause()
        elapsedTime += SystemClock.elapsedRealtime() - startTime
    }

    override fun onResume() {
        super.onResume()
        startTime = SystemClock.elapsedRealtime()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("elapsedTime" , elapsedTime)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        elapsedTime = savedInstanceState.getLong("elapsedTime")
    }
}