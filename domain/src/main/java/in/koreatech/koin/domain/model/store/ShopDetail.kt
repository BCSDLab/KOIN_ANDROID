package `in`.koreatech.koin.domain.model.store

data class ShopDetail(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val address: String,
    val openTime: String?,
    val closeTime: String?,
    val closedDays: List<String>,
    val phone: String,
    val introduction: String?,
    val notice: String?,
    val deliveryTips: List<ShopDetailDeliveryTips>,
    val ownerInfo: ShopDetailOwnerInfo,
    val origins: List<ShopDetailOrigins>
) {
    data class ShopDetailDeliveryTips(
        val fromAmount: Int,
        val toAmount: Int,
        val fee: Int
    )

    data class ShopDetailOwnerInfo(
        val name: String?,
        val shopName: String?,
        val address: String?,
        val companyRegistrationNumber: String?
    )

    data class ShopDetailOrigins(
        val ingredient: String,
        val origin: String
    )
}
