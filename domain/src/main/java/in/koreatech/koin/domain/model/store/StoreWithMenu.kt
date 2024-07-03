package `in`.koreatech.koin.domain.model.store

data class StoreWithMenu(
    val uid: Int,
    val name: String,
    val phone: String,
    val address: String?,
    val description: String?,
    val isDeliveryOk: Boolean,
    val deliveryPrice: Int,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val open: Store.OpenData,
    val imageUrls: List<String>?,
    val updateAt: String,
    val isEvent: Boolean?,
    val shopCategories: List<Category>?,
    val menuCategories: List<Category>?,
    val bank: String?,
    val accountNumber: String?
) {
    data class Category(
        val id: Int,
        val name: String
    )
}