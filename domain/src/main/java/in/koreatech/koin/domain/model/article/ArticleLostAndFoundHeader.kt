package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFoundHeader(
    val id: Int,
    val boardId: Int,
    val type: String,
    val category: String,
    val foundPlace: String,
    val foundDate: String,
    val content: String?,
    val author: String,
    val isReported: Boolean,
    val isFound: Boolean,
    val registeredAt: String,
    val updatedAt: String
)
