package `in`.koreatech.koin.core.analytics

import android.os.SystemClock

object EventUtils {
    private const val SCROLL_THRESHOLD = 70f
    private var startTime = 0L
    private var elapsedTime = 0L

    fun didCrossedScrollThreshold(
        prevScrollRatio: Float,
        currentScrollRatio: Float,
        threshold: Float = SCROLL_THRESHOLD
    ): Boolean {
        return prevScrollRatio < threshold && currentScrollRatio >= threshold
    }

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
}
