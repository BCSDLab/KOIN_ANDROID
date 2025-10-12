package `in`.koreatech.koin.feature.setting.util

import `in`.koreatech.koin.domain.model.notification.Subscribes
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

fun List<Subscribes>.isTypePermitted(type : SubscribesType): Boolean {
    this.forEach { subscribe ->
        if (subscribe.type == type) return subscribe.isPermit
    }
    return false
}
fun List<Subscribes>.isDetailTypePermitted(type : SubscribesDetailType): Boolean {
    this.forEach { subscribe ->
        subscribe.detailSubscribes.forEach { subscribeDetail ->
            if (subscribeDetail.type == type) return subscribeDetail.isPermit
        }
    }
    return false
}