package `in`.koreatech.koin.feature.store

import `in`.koreatech.koin.domain.model.store.LegacyShopMenus
import `in`.koreatech.koin.domain.model.store.Store
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

val dummyShopMenus = listOf(
    LegacyShopMenus(
        id = 1,
        name = "김치찌개",
        isSingle = true,
        singlePrice = 8000,
        description = "매콤한 김치찌개",
        imageUrls = listOf("https://example.com/korean_food1.jpg"),
        isHidden = false,
        optionPrices = null
    )
)

val dummyStoreMenuCategories = StoreMenuCategories(
    id = 1,
    name = "메인메뉴",
    menus = dummyShopMenus
)

private val dummyOpenData = Store.OpenData(
    openTime = "09:00",
    closeTime = "21:00",
    dayOfWeek = "월요일",
    closed = false
)

val dummyStoreWithMenu = StoreWithMenu(
    uid = 1,
    name = "더미 가게",
    phone = "010-1234-5678",
    address = "서울시 강남구 역삼동 123-45",
    description = "테스트용 더미 가게입니다.",
    isDeliveryOk = true,
    deliveryPrice = 2000,
    isCardOk = true,
    isBankOk = false,
    open = dummyOpenData,
    imageUrls = listOf("https://example.com/image1.jpg", "https://example.com/image2.jpg"),
    updateAt = "2024-06-01T12:00:00",
    isEvent = false,
    shopCategories = listOf(
        StoreWithMenu.Category(id = 1, name = "한식"),
        StoreWithMenu.Category(id = 2, name = "분식")
    ),
    menuCategories = listOf(
        StoreWithMenu.Category(id = 10, name = "메인메뉴"),
        StoreWithMenu.Category(id = 11, name = "사이드메뉴")
    ),
    bank = "국민은행",
    accountNumber = "123-456-7890"
)
