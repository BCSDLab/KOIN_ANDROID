package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.util.ext.second
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow

fun <T> getTimerTickFlow(period: Long = 60.second, data: () -> T) = flow {
    var time = System.currentTimeMillis()
    while (true) {
        System.currentTimeMillis().let {
            if (it - time >= period) {
                emit(data())
                time += period
            } else delay(period / 100)
        }
    }
}

fun getTimerTickFlow(period: Long = 60.second) = getTimerTickFlow {  }