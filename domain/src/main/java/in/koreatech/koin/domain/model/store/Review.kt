package `in`.koreatech.koin.domain.model.store

data class Review(
    val rating: Int,
    val content: String,
    val imageUrls: List<String>?,
    val menuNames: List<String>?,
)