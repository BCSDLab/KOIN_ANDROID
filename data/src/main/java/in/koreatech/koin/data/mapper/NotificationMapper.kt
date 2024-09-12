package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.notification.NotificationPermissionInfoResponse
import `in`.koreatech.koin.data.response.notification.SubscribeDetailResponse
import `in`.koreatech.koin.data.response.notification.SubscribesResponse
import `in`.koreatech.koin.domain.model.notification.NotificationPermissionInfo
import `in`.koreatech.koin.domain.model.notification.Subscribes
import `in`.koreatech.koin.domain.model.notification.SubscribesDetail
import `in`.koreatech.koin.domain.model.notification.SubscribesDetailType
import `in`.koreatech.koin.domain.model.notification.SubscribesType

fun NotificationPermissionInfoResponse.toNotificationPermissionInfo() = NotificationPermissionInfo(
    isPermit = isPermit,
    subscribes = subscribes.map { it.toSubscribes() },
)

fun SubscribesResponse.toSubscribes() = Subscribes(
    type = type.toSubscribesType(),
    isPermit = isPermit,
    detailSubscribes = detailSubscribes.map { it.toSubscribesDetail() }
)

fun SubscribeDetailResponse.toSubscribesDetail() = SubscribesDetail(
    type = detailType.toSubscribesDetailType(),
    isPermit = isPermit
)

fun String.toSubscribesType(): SubscribesType = when (this) {
    Subscribes.SHOP_EVENT -> SubscribesType.SHOP_EVENT
    Subscribes.DINING_SOLD_OUT -> SubscribesType.DINING_SOLD_OUT
    Subscribes.DINING_IMAGE_UPLOAD -> SubscribesType.DINING_IMAGE_UPLOAD
    else -> SubscribesType.NOTHING
}

fun String.toSubscribesDetailType(): SubscribesDetailType = when (this) {
    SubscribesDetail.BREAKFAST -> SubscribesDetailType.BREAKFAST
    SubscribesDetail.LUNCH -> SubscribesDetailType.LUNCH
    SubscribesDetail.DINNER -> SubscribesDetailType.DINNER
    else -> SubscribesDetailType.NOTHING
}