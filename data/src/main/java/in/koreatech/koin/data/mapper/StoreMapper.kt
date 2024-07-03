package `in`.koreatech.koin.data.mapper

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.response.store.ShopMenuOptionsResponse
import `in`.koreatech.koin.data.response.store.ShopMenusResponse
import `in`.koreatech.koin.data.response.store.StoreCategoriesItemResponse
import `in`.koreatech.koin.data.response.store.StoreDetailEventResponse
import `in`.koreatech.koin.data.response.store.StoreEventItemReponse
import `in`.koreatech.koin.data.response.store.StoreItemResponse
import `in`.koreatech.koin.data.response.store.StoreItemWithMenusResponse
import `in`.koreatech.koin.data.response.store.StoreMenuCategoriesResponse
import `in`.koreatech.koin.data.response.store.StoreMenuResponse
import `in`.koreatech.koin.domain.model.store.ShopEvent
import `in`.koreatech.koin.domain.model.store.ShopEvents
import `in`.koreatech.koin.domain.model.store.ShopMenus
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreCategories
import `in`.koreatech.koin.domain.model.store.StoreEvent
import `in`.koreatech.koin.domain.model.store.StoreMenu
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreWithMenu
import `in`.koreatech.koin.domain.model.store.toStoreCategory
import `in`.koreatech.koin.domain.util.ext.localDayOfWeekName

fun StoreItemResponse.toStore(): Store = Store(
    uid = uid,
    name = name,
    phone = phone ?: "",
    isDeliveryOk = isDeliveryOk ?: false,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    isEvent = isEvent ?: false,
    isOpen = isOpen ?: false,
    open = open.filter { it.dayOfWeek == localDayOfWeekName }.map {
        Store.OpenData(
            dayOfWeek = it.dayOfWeek,
            closed = it.closed,
            openTime = it.openTime ?: "",
            closeTime = it.closeTime ?: ""
        )
    }.getOrElse(0) {Store.OpenData(localDayOfWeekName, false, "00:00", "00:00")},
    categoryIds = categoryIds.map { it.toStoreCategory() }
)

fun StoreEventItemReponse.toStoreEvent(): StoreEvent = StoreEvent(
    shopId = shopId,
    shopName = shopName ?: "",
    eventId = eventId,
    title = title ?: "",
    content = content ?: "",
    thumbnailImages = thumbnailImages ?: ArrayList<String>(),
    startDate = startDate ?: "",
    endDate = endDate ?: ""
)

fun StoreCategoriesItemResponse.toStoreCategories(): StoreCategories = StoreCategories(
    id = id,
    imageUrl = imageUrl,
    name = name
)

fun StoreItemWithMenusResponse.toStoreWithMenu(): StoreWithMenu = StoreWithMenu(
    uid = uid,
    name = name,
    phone = phone ?: "",
    address = address ?: "",
    description = description?.replace("\\n", System.getProperty("line.separator") ?: "\n"),
    isDeliveryOk = isDeliveryOk ?: false,
    deliveryPrice = deliveryPrice ?: 0,
    isCardOk = isCardOk ?: false,
    isBankOk = isBankOk ?: false,
    updateAt = updateAt,
    isEvent = isEvent ?: false,
    open = open?.filter { it.dayOfWeek == localDayOfWeekName }?.map {
        Store.OpenData(
            dayOfWeek = it.dayOfWeek,
            closed = it.closed,
            openTime = it.openTime ?: "",
            closeTime = it.closeTime ?: ""
        )
    }.orEmpty().getOrElse(0) {Store.OpenData(localDayOfWeekName, false, "00:00", "00,00")},
    imageUrls = imageUrls ?: emptyList(),
    shopCategories = shopCategories?.map { it.toCategory() }.orEmpty(),
    menuCategories = menuCategories?.map { it.toCategory() }.orEmpty(),
    bank = bank ?: null,
    accountNumber = accountNumber ?: null
)

fun StoreItemWithMenusResponse.CategoriesResponseDTO.toCategory() = StoreWithMenu.Category(
    id = id,
    name = name
)

fun StoreMenuResponse.toStoreMenu() = StoreMenu(
    menuCategories = menuCategories?.map { it.toStoreMenuCategories() }.orEmpty()
)

fun StoreMenuCategoriesResponse.toStoreMenuCategories() = StoreMenuCategories(
    id = id,
    name = name,
    menus = menus?.map { it.toShopMenus() }.orEmpty()
)
fun ShopMenusResponse.toShopMenus() = ShopMenus(
    id = id,
    name = name,
    isHidden = isHidden,
    isSingle = isSingle,
    singlePrice = singlePrice,
    optionPrices =  optionPrices?.map { it.toShopMenuOptions() }.orEmpty(),
    description = description,
    imageUrls = imageUrls.orEmpty()
)

fun ShopMenuOptionsResponse.toShopMenuOptions() = ShopMenus.ShopMenuOptions(
    option = option ?: "",
    price = price
)

fun StoreDetailEventResponse.toStoreDetailEvents(): ShopEvents = ShopEvents(
    events = events?.map { it.toStoreDetailEvent() }.orEmpty()
)

fun StoreDetailEventResponse.StoreEventDTO.toStoreDetailEvent() = ShopEvent(
    shopId = shopId ?: 0,
    shopName = shopName ?: "",
    eventId = eventId ?: 0,
    title = title ?: "",
    content = content ?: "",
    thumbnailImages = thumbnailImages ?: emptyList(),
    startDate = startDate ?: "",
    endDate = endDate ?: ""
)