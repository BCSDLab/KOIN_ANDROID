package `in`.koreatech.koin.domain.model.store

data class StoreWithMenuV2(
    val uid: Int,
    val name: String,
    val phone: String,
    val address: String?,
    val description: String?,
    val isDeliveryOk: Boolean,
    val deliveryPrice: Int,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val imageUrls: List<String>?,
    val updateAt: String,
    val isEvent: Boolean?,
    val shopCategories: List<StoreWithMenu.Category>?,
    val menuCategories: List<StoreWithMenu.Category>?,
    val bank: String?,
    val accountNumber: String?,
    val openTime: String,
    val closeTime: String
) {
    companion object {
        fun empty() = StoreWithMenuV2(
            uid = 0,
            name = "",
            address = "",
            phone = "",
            description = "",
            isBankOk = false,
            isDeliveryOk = false,
            isCardOk = false,
            deliveryPrice = 0,
            imageUrls = emptyList(),
            updateAt = "",
            isEvent = false,
            shopCategories = emptyList(),
            menuCategories = emptyList(),
            bank = null,
            accountNumber = null,
            openTime = "",
            closeTime = ""
        )
    }
}
