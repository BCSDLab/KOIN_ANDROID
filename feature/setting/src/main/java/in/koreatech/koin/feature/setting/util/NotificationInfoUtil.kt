package `in`.koreatech.koin.feature.setting.util

import androidx.compose.ui.util.fastForEach
import `in`.koreatech.koin.domain.model.notification.Subscribes
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

fun List<Subscribes>.isTypePermitted(type: SubscribesType): Boolean {
    this.fastForEach { subscribe ->
        if (subscribe.type == type) return subscribe.isPermit
    }
    return false
}
fun List<Subscribes>.isDetailTypePermitted(type: SubscribesDetailType): Boolean {
    this.fastForEach { subscribe ->
        subscribe.detailSubscribes.forEach { subscribeDetail ->
            if (subscribeDetail.type == type) return subscribeDetail.isPermit
        }
    }
    return false
}
