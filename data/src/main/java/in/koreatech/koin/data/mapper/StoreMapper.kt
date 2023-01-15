package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.constant.STORE_OPEN_TIME_FORMAT
import `in`.koreatech.koin.data.constant.STORE_UPDATED_DATE_TIME_FORMAT
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreMenuPrice
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun StoreItemResponse.toStore(): Store = Store(
    id = uid,
    name = name,
    chosung = chosung,
    category = category.toStoreCategory(),
    phoneNumber = phone,
    openTime = if (openTime == null) {
        LocalTime.MIN
    } else {
        LocalTime.parse(openTime, DateTimeFormatter.ofPattern(STORE_OPEN_TIME_FORMAT))
    },
    closeTime = if (closeTime == null) {
        LocalTime.MAX
    } else {
        LocalTime.parse(closeTime, DateTimeFormatter.ofPattern(STORE_OPEN_TIME_FORMAT))
    },
    address = address,
    description = description,
    isDeliveryOk = isDeliveryOk ?: false,
    deliveryPrice = deliveryPrice ?: 0,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    images = imageUrls ?: emptyList(),
    updatedAt = LocalDateTime.parse(
        updatedAt,
        DateTimeFormatter.ofPattern(STORE_UPDATED_DATE_TIME_FORMAT)
    )
)

fun StoreMenuResponse.toStoreMenu(): StoreMenu = StoreMenu(
    id = id,
    shopId = shopId,
    name = name,
    priceType = priceType.map { StoreMenuPrice(sizeName = it.size, price = it.price.toInt()) },
    createdAt = LocalDateTime.parse(
        createdAt,
        DateTimeFormatter.ofPattern(STORE_UPDATED_DATE_TIME_FORMAT)
    ),
    updatedAt = LocalDateTime.parse(
        updatedAt,
        DateTimeFormatter.ofPattern(STORE_UPDATED_DATE_TIME_FORMAT)
    )
)