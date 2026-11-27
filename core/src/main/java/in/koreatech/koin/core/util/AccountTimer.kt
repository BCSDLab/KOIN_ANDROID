package `in`.koreatech.koin.core.util

import android.os.CountDownTimer

object AccountTimer {
    const val NO_TOKEN = 0L

    @Volatile
    var secondsRemaining: Long = 180

    private var timer: CountDownTimer? = null

    private var currentToken: Long = NO_TOKEN

    fun start(tick: (Int) -> Unit): Long = start(180, tick)

    fun start(seconds: Long, tick: (Int) -> Unit): Long {
        val newTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                tick(secondsRemaining.toInt())
            }

            override fun onFinish() {
                tick(0)
            }
        }
        val previousTimer: CountDownTimer?
        val token: Long
        synchronized(this) {
            previousTimer = timer
            timer = newTimer
            secondsRemaining = seconds
            token = ++currentToken
            newTimer.start()
        }
        previousTimer?.cancel()
        return token
    }

    fun cancel() {
        val detachedTimer = synchronized(this) {
            timer.also { timer = null }
        }
        detachedTimer?.cancel()
    }

    fun cancel(token: Long) {
        val detachedTimer = synchronized(this) {
            if (token == NO_TOKEN || token != currentToken) return
            timer.also { timer = null }
        }
        detachedTimer?.cancel()
    }
}
