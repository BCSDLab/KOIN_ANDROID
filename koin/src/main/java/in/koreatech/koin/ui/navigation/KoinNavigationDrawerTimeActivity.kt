package `in`.koreatech.koin.ui.navigation

import android.os.SystemClock
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class KoinNavigationDrawerTimeActivity : KoinNavigationDrawerActivity() {

    private var startTime = 0L
    private var elapsedTime = 0L

    fun getElapsedTimeAndReset(): Double {
        val time = SystemClock.elapsedRealtime() - startTime + elapsedTime
        reset()
        return time / 1000.0
    }

    fun getElapsedTime(): Double {
        return (SystemClock.elapsedRealtime() - startTime + elapsedTime) / 1000.0
    }

    private fun reset() {
        startTime = SystemClock.elapsedRealtime()
        elapsedTime = 0
    }

    override fun onPause() {
        super.onPause()
        elapsedTime += SystemClock.elapsedRealtime() - startTime
    }

    override fun onResume() {
        super.onResume()
        startTime = SystemClock.elapsedRealtime()
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putLong("elapsedTime" , elapsedTime)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        elapsedTime = savedInstanceState.getLong("elapsedTime")
//    }
}