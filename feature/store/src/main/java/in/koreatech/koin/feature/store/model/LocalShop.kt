package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.util.DateFormatUtil
import `in`.koreatech.koin.domain.util.ext.HHMM
import `in`.koreatech.koin.domain.util.ext.localTimeNow
import `in`.koreatech.koin.feature.store.enums.FilterBadge
import java.time.LocalTime
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalShop(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val filterBadgeList: List<FilterBadge>,
    val minimumOrderAmount: Int,
    val ratingAverage: Double,
    val reviewCount: Int,
    val minimumDeliveryTip: Int,
    val maximumDeliveryTip: Int,
    val isOpen: Boolean,
    val categoryIds: List<Int>,
    val imageUrls: List<String>,
    val open: List<LocalOrderStoreShopsOpen>,
    val openStatus: String
) : Parcelable {
    @Parcelize
    data class LocalOrderStoreShopsOpen(
        val dayOfWeek: Int,
        val closed: Boolean,
        val openTime: String,
        val closeTime: String
    ) : Parcelable
}

internal fun Shop.toLocalShop(): LocalShop {
    return LocalShop(
        shopId = shopId,
        orderableShopId = orderableShopId,
        name = name,
        filterBadgeList = listOfNotNull(
            if (isTakeoutAvailable) FilterBadge.PICKUP_AVAILABLE else null,
            if (isDeliveryAvailable) FilterBadge.DELIVERY_AVAILABLE else null,
            if (serviceEvent) FilterBadge.SERVICE else null
        ),
        minimumOrderAmount = minimumOrderAmount,
        ratingAverage = ratingAverage,
        reviewCount = reviewCount,
        minimumDeliveryTip = minimumDeliveryTip,
        maximumDeliveryTip = maximumDeliveryTip,
        isOpen = isOpen,
        categoryIds = categoryIds,
        imageUrls = imageUrls,
        open = open.map { it.toLocalOrderStoreShopsOpen() },
        openStatus = openStatus
    )
}

internal fun Shop.OrderStoreShopsOpen.toLocalOrderStoreShopsOpen(): LocalShop.LocalOrderStoreShopsOpen {
    return LocalShop.LocalOrderStoreShopsOpen(
        dayOfWeek = dayOfWeek,
        closed = closed,
        openTime = openTime,
        closeTime = closeTime
    )
}

internal fun Store.toLocalShop(): LocalShop {
    return LocalShop(
        shopId = uid,
        orderableShopId = uid,
        name = name,
        filterBadgeList = emptyList(),
        minimumOrderAmount = 0,
        ratingAverage = averageRate,
        reviewCount = reviewCount,
        minimumDeliveryTip = 0,
        maximumDeliveryTip = 0,
        isOpen = isOpen,
        categoryIds = categoryIds,
        imageUrls = emptyList(),
        open = listOf(open.toLocalOrderStoreShopsOpen()),
        openStatus = open.toOpenStatus()
    )
}

// Old Api doesn't return open status, so we need to calculate it manually
internal fun Store.OpenData.toOpenStatus(): String {
    return if (closed) {
        val closeTime = LocalTime.parse(closeTime)
        val currentTime = LocalTime.parse(localTimeNow.HHMM)

        if (closeTime.isBefore(currentTime)) {
            "CLOSED"
        } else {
            "PREPARING"
        }
    } else {
        "OPERATING"
    }
}

internal fun Store.OpenData.toLocalOrderStoreShopsOpen(): LocalShop.LocalOrderStoreShopsOpen {
    return LocalShop.LocalOrderStoreShopsOpen(
        dayOfWeek = DateFormatUtil.dayOfWeekToIndex(dayOfWeek),
        closed = closed,
        openTime = openTime,
        closeTime = closeTime
    )
}
