package `in`.koreatech.koin.feature.store.reviewEdit

import `in`.koreatech.koin.feature.store.model.StoreNavigationData

data class ReviewEditState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(shopId = -1, orderableShopId = -1, isOrderableShop = false),
    val storeId: Int = 0,
    val storeName: String = "",
    val reviewId: Int = 0,
    val rating: Int = 0,
    val reviewContent: String = "",
    val menuTag: String = "",
    val menuTags: List<String> = emptyList(),
    val imageUris: List<String> = emptyList(),
    val presignedPairs: List<Triple<String, String, String>> = emptyList(),
    val imageUploadUrls: List<String> = emptyList()
)
