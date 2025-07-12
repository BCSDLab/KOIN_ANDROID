package `in`.koreatech.koin.domain.model.ordershop

data class OrderShop(
    val shopId: Int,
    val orderableShopId: Int,
    val name: String,
    val address: String,
    val openTime: String,
    val closeTime: String,
    val closedDays: List<String>,
    val phone: String?,
    val introduction: String?,
    val notice: String?,
    val deliveryTips: List<DeliveryTip>,
    val ownerInfo: OwnerInfo,
    val origins: List<String>
) {
    data class DeliveryTip(
        val orderPrice: Int,
        val deliveryTip: Int
    )

    data class OwnerInfo(
        val name: String?,
        val shopName: String?,
        val address: String?,
        val companyRegistrationNumber: String?
    )

    companion object {
        fun empty() = OrderShop(
            shopId = -1,
            orderableShopId = -1,
            name = "",
            address = "",
            openTime = "",
            closeTime = "",
            closedDays = emptyList(),
            phone = null,
            introduction = null,
            notice = null,
            deliveryTips = emptyList(),
            ownerInfo = OwnerInfo(
                name = null,
                shopName = null,
                address = null,
                companyRegistrationNumber = null
            ),
            origins = emptyList()
        )
    }
}
