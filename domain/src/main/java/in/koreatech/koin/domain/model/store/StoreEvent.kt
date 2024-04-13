package `in`.koreatech.koin.domain.model.store

data class StoreEvent(
    val shop_id: Int,
    val shop_name: String,
    val event_id: Int,
    val title: String,
    val content: String,
    val thumbnail_images: List<String>?,
    val start_date: String,
    val end_date: String
)