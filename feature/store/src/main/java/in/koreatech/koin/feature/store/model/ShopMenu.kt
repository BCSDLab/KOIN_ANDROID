package `in`.koreatech.koin.feature.store.model

/**
 * 상점 메뉴 모델
 */

data class MenuCategoryModel(
    val menuGroupId: Int,
    val menuGroupName: String,
    val menus: List<MenuModel>,
    val isChecked: Boolean = false,
)

data class MenuModel(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnailImage: String,
    val isSoldOut: Boolean,
    val prices: List<MenuPriceModel>,
    val isSingle: Boolean, // 단일 메뉴인지 여부
    val singlePrice: Int? = null // 단일 가격이 있는 경우에만 사용
)

data class MenuPriceModel(
    val id: Int?,
    val name: String?, // null이면 단일 가격
    val price: Int?
)


/**
 * 상점 정보 모델
 */
data class ShopInfoModel(
    val shopId: Int,
    val orderableShopId: Int?,
    val name: String,
    val imageUrls: List<String>?,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val bank: String?,
    val isDeliveryAvailable: Boolean,
    val isTakeoutAvailable: Boolean,
    val minimumOrderAmount: Int?,
    val minimumDeliveryTip: Int?,
    val maximumDeliveryTip: Int?,
) {
    companion object {
        fun empty() = ShopInfoModel(
            shopId = 0,
            orderableShopId = null,
            name = "",
            imageUrls = emptyList(),
            isCardOk = false,
            isBankOk = false,
            bank = null,
            isDeliveryAvailable = false,
            isTakeoutAvailable = false,
            minimumOrderAmount = null,
            minimumDeliveryTip = null,
            maximumDeliveryTip = null
        )
    }
}

/**
 * 가게 정보 원산지 모델
 */
data class StoreDescriptionModel(
    val id: Int,
    val storeName: String,
    val description: String?,
) {
    companion object {
        fun empty() = StoreDescriptionModel(
            id = 0,
            storeName = "",
            description = null
        )
    }
}