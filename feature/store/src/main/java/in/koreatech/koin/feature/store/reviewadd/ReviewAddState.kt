package `in`.koreatech.koin.feature.store.reviewadd

import `in`.koreatech.koin.domain.model.store.StoreUrl
import `in`.koreatech.koin.feature.store.model.StoreNavigationData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class ReviewAddState(
    val isLoading: Boolean = false,
    val storeNavigationData: StoreNavigationData = StoreNavigationData(shopId = -1, orderableShopId = -1, isOrderableShop = false),
    val storeId: Int = 0,
    val storeName: String = "",
    val rating: Int = 1,
    val reviewContent: String = "",
    val menuTag: String = "",
    val menuTags: ImmutableList<String> = persistentListOf(),
    val imageUris: ImmutableList<String> = persistentListOf(),
    val presignedPairs: ImmutableList<PresignedPair> = persistentListOf(),
    val fileInfo: MutableList<StoreUrl> = mutableListOf()
)

data class PresignedPair(
    val imageUri: String,
    val preSignedUrl: String,
    val fileUrl: String
)
