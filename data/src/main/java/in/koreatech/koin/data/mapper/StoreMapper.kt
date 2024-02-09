package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreMenuPrice
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.domain.util.ext.localDayOfWeekName

fun StoreItemResponse.toStore(): Store = Store(
    uid = uid,
    name = name,
    phone = phone ?: "000-0000-0000",
    isDeliveryOk = isDeliveryOk ?: false,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    open = open.filter { it.dayOfWeek == localDayOfWeekName }.map {
        Store.OpenData(
            dayOfWeek = it.dayOfWeek,
            closed = it.closed,
            openTime = it.openTime ?: "00:00",
            closeTime = it.closeTime ?: "00:00"
        )
    }.first(),
    categoryIds = categoryIds.map { it.toStoreCategory() }
)

fun StoreMenuResponse.toStoreMenu(): StoreMenu = StoreMenu(
    id = id,
    shopId = shopId,
    name = name,
    priceType = priceType.map { StoreMenuPrice(sizeName = it.size, price = it.price.toInt()) }
)

//fun StoreItemWithMenusResponse.toStoreWithMenu(): StoreWithMenu = StoreWithMenu(
//    store = Store(
//        id = uid,
//        name = name,
//        chosung = chosung,
//        category = category.toStoreCategory(),
//        phoneNumber = phone,
//        openTime = if (openTime == null) {
//            LocalTime.MIN
//        } else {
//            LocalTime.parse(openTime, DateTimeFormatter.ofPattern(STORE_OPEN_TIME_FORMAT))
//        },
//        closeTime = if (closeTime == null) {
//            LocalTime.MAX
//        } else {
//            LocalTime.parse(closeTime, DateTimeFormatter.ofPattern(STORE_OPEN_TIME_FORMAT))
//        },
//        address = address,
//        description = description?.replace("\\n", System.getProperty("line.separator") ?: "\n"),
//        isDeliveryOk = isDeliveryOk ?: false,
//        deliveryPrice = deliveryPrice ?: 0,
//        isCardOk = isCardOk ?: false,
//        isBankOk = isBankOk ?: false,
//        images = imageUrls ?: emptyList(),
//        updatedAt = LocalDateTime.parse(
//            updatedAt,
//            DateTimeFormatter.ofPattern(STORE_UPDATED_DATE_TIME_FORMAT)
//        )
//    ),
//    storeMenus = menus.map { it.toStoreMenu() }
//)