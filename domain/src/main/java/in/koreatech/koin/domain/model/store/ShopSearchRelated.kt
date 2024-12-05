package `in`.koreatech.koin.domain.model.store

data class ShopSearchRelated (
    val keyword: String,
    val shopIds: List<Int>,
    val shopId: Int?,
)

data class ShopSearchRelatedList(
    val keywords: List<ShopSearchRelated>
)