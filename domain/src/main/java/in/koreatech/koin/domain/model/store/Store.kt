package `in`.koreatech.koin.domain.model.store

data class Store(
    val uid: Int,
    val name: String,
    val phone: String,
    val isDeliveryOk: Boolean,
    val isCardOk: Boolean,
    val isBankOk: Boolean,
    val open: OpenData,
    val categoryIds: List<StoreCategory?>,
) {
    data class OpenData(
        val dayOfWeek: String,
        val closed: Boolean,
        val openTime: String,
        val closeTime: String,
    )
}