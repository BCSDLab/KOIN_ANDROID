package `in`.koreatech.koin.feature.store.model

import `in`.koreatech.koin.domain.model.store.OrderableShopSearchRelated
import kotlinx.serialization.Serializable

@Serializable
data class LocalShopSearchResult(
    val shopId: Int,
    val keyword: String,
    val shopName: String,
    val isShop: Boolean
)

fun OrderableShopSearchRelated.OrderableShopSearchShopNameResult.toLocalShopSearchResult(): LocalShopSearchResult =
    LocalShopSearchResult(
        shopId = orderableShopId,
        keyword = orderableShopName,
        shopName = orderableShopName,
        isShop = true
    )

fun OrderableShopSearchRelated.OrderableShopSearchMenuNameResult.toLocalShopSearchResult(): LocalShopSearchResult =
    LocalShopSearchResult(
        shopId = orderableShopId,
        keyword = "$orderableShopName | $menuName",
        shopName = orderableShopName,
        isShop = false
    )
