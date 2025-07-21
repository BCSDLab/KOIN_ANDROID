package `in`.koreatech.koin.feature.store.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.store.OpenStatus
import `in`.koreatech.koin.domain.model.store.Shop
import `in`.koreatech.koin.feature.store.enums.FilterBadge
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
    val openStatus: OpenStatus
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
