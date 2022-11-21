package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.util.ext.second
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow

fun <T> getTimerTickFlow(period: Long = 1.second, data: (epochSeconds: Long) -> T) = flow {
    var time = System.currentTimeMillis()
    var count = 0L
    while (true) {
        System.currentTimeMillis().let {
            if (it - time >= period) {
                emit(data(count))
                time += period
                count++
            } else delay(period / 100)
        }
    }
}

fun getTimerTickFlow(period: Long = 1.second) = getTimerTickFlow {  }