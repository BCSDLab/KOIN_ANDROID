package `in`.koreatech.koin.feature.signup.util

import android.os.CountDownTimer

object AccountTimer {
    var secondsRemaining: Long = 180
    private var timer: CountDownTimer? = null

    fun start(tick: (Int) -> Unit) {
        cancel()
        secondsRemaining = 180
        timer =
            object : CountDownTimer(secondsRemaining * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    secondsRemaining = millisUntilFinished / 1000
                    tick(secondsRemaining.toInt())
                }

                override fun onFinish() {
                    tick(0)
                }
            }.also { it.start() }
    }

    fun cancel() {
        timer?.cancel()
        timer = null
    }
}
