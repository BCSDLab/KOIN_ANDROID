package `in`.koreatech.koin.ui.navigation

import android.os.Bundle
import android.os.SystemClock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class KoinNavigationDrawerTimeActivity : KoinNavigationDrawerActivity() {

    private var startTime = 0L
    private var resumedTime = 0L
    protected var elapsedTime: Long = 0
        get() {
            val time = SystemClock.elapsedRealtime() - startTime + resumedTime
            startTime = SystemClock.elapsedRealtime()
            resumedTime = 0
            return time
        }


    override fun onPause() {
        super.onPause()
        resumedTime += SystemClock.elapsedRealtime() - startTime
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