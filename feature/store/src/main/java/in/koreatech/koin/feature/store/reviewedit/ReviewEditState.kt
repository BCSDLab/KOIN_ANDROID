package `in`.koreatech.koin.feature.store.reviewedit

import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ReviewEditState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(shopId = -1, orderableShopId = -1, isOrderableShop = false),
    val storeId: Int = 0,
    val storeName: String = "",
    val reviewId: Int = 0,
    val rating: Int = 1,
    val reviewContent: String = "",
    val menuTag: String = "",
    val menuTags: ImmutableList<String> = persistentListOf(),
    val imageUris: ImmutableList<String> = persistentListOf(),
    val presignedPairs: ImmutableList<PresignedPair> = persistentListOf()
)

data class PresignedPair(
    val imageUri: String,
    val preSignedUrl: String,
    val fileUrl: String
)
