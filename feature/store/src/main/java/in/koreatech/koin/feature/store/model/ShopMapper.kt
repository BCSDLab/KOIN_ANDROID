package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.ordershop.OrderMenuList
import `in`.koreatech.koin.domain.model.ordershop.OrderShopSummary
import `in`.koreatech.koin.domain.model.store.StoreMenuCategories
import `in`.koreatech.koin.domain.model.store.StoreWithMenu

/**
 * 가게 정보 모델을 UI 모델로 변환하는 확장 함수들
 */
fun StoreWithMenu.toStoreInfoModel(): ShopInfoModel { // 기존 상점 정보 모델을 ShopInfoModel로 변환
    return ShopInfoModel(
        shopId = uid,
        orderableShopId = null,
        name = name,
        imageUrls = imageUrls ?: emptyList(),
        isCardOk = isCardOk,
        isBankOk = isBankOk,
        bank = bank,
        isDeliveryAvailable = isDeliveryOk,
        isTakeoutAvailable = false,
        minimumOrderAmount = null,
        minimumDeliveryTip = deliveryPrice,
        maximumDeliveryTip = null
    )
}

fun OrderShopSummary.toStoreIndoModel(): ShopInfoModel { // 주문 가능한 상점 정보를 ShopInfoModel로 변환
    return ShopInfoModel(
        shopId = shopId,
        orderableShopId = orderableShopId,
        name = name,
        imageUrls = images,
        isCardOk = payCard,
        isBankOk = payBank,
        bank = null,
        isDeliveryAvailable = isDeliveryAvailable,
        isTakeoutAvailable = isTakeoutAvailable,
        minimumOrderAmount = minimumOrderAmount,
        minimumDeliveryTip = minimumDeliveryTip,
        maximumDeliveryTip = maximumDeliveryTip
    )
}

/**
 * 상점 메뉴 목록을 MenuCategoryModel로 변환하는 확장 함수들
 */

fun StoreMenuCategories.toMenuCategoryModel(): MenuCategoryModel { // 기존 상점 메뉴 목록을 MenuCategoryModel로 변환
    return MenuCategoryModel(
        menuGroupId = id,
        menuGroupName = name ?: "",
        menus = menus?.map { menu ->
            MenuModel(
                id = menu.id,
                name = menu.name,
                description = menu.description,
                thumbnailImage = menu.imageUrls?.firstOrNull(),
                isSoldOut = false,
                prices = menu.optionPrices?.map { price ->
                    MenuPriceModel(
                        id = null,
                        name = price.option,
                        price = price.price
                    )
                } ?: emptyList(),
                singlePrice = menu.singlePrice,
                isSingle = menu.isSingle
            )
        } ?: emptyList()
    )
}

fun OrderMenuList.toMenuCategoryModel(): MenuCategoryModel { // 주문 가능한 메뉴 목록을 MenuCategoryModel로 변환
    return MenuCategoryModel(
        menuGroupId = menuGroupId,
        menuGroupName = menuGroupName,
        menus = menus.map { orderMenu ->
            MenuModel(
                id = orderMenu.id,
                name = orderMenu.name,
                description = orderMenu.description,
                thumbnailImage = orderMenu.thumbnailImage,
                isSoldOut = orderMenu.isSoldOut,
                prices = orderMenu.prices.map { price ->
                    MenuPriceModel(
                        id = price.id,
                        name = price.name,
                        price = price.price
                    )
                },
                singlePrice = orderMenu.prices.getOrNull(0)?.price,
                isSingle = menus.getOrNull(0)?.name == null // 옵션 이름이 null이면 단일 메뉴로 간주
            )
        }
    )
}
