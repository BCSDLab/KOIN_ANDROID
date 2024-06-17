package `in`.koreatech.koin.domain.model.owner.insertstore

data class StoreBasicInfo(
    val storeName: String ="",
    val storeAddress: String ="",
    val storeImage: String ="",
    val storeCategory: Int = -1
)
