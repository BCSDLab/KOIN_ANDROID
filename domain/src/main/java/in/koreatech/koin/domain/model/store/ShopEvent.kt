package `in`.koreatech.koin.domain.model.store

data class ShopEvent(
    val shopId: Int,
    val shopName: String,
    val eventId: Int,
    val title: String,
    val content: String,
    val thumbnailImages: List<String>?,
    val startDate: String,
    val endDate: String
)