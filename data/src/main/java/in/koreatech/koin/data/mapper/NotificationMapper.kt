package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.notification.SubscribesResponse
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.Subscribes
import `in`.koreatech.koin.domain.model.notification.SubscribesType

fun NotificationPermissionInfoResponse.toNotificationPermissionInfo() = NotificationPermissionInfo(
    isPermit = isPermit,
    subscribes = subscribes.map { it.toSubscribes() }
)

fun SubscribesResponse.toSubscribes() = Subscribes(
    type = type.toSubscribesType(),
    isPermit = isPermit
)

fun String.toSubscribesType(): SubscribesType = when (this) {
    Subscribes.SHOP_EVENT -> SubscribesType.SHOP_EVENT
    Subscribes.DINING_SOLD_OUT -> SubscribesType.DINING_SOLD_OUT
    else -> SubscribesType.NOTHING
}

fun SubscribesType.toString(): String = when (this) {
    SubscribesType.SHOP_EVENT -> Subscribes.SHOP_EVENT
    SubscribesType.DINING_SOLD_OUT -> Subscribes.DINING_SOLD_OUT
    else -> ""
}
