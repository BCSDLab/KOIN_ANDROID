package `in`.koreatech.koin.domain.model.store

data class ReviewDetail(
    val reviewId: Int,
    val rating: Int,
    val nickName: String,
    val content: String,
    val imageUrls: List<String>,
    val menuNames: List<String>,
    val isModified: Boolean,
    val createdAt: String
)
