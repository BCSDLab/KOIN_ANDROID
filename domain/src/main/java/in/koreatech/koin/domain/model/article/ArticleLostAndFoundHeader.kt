package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFoundHeader(
    val id: Int,
    val boardId: Int,
    val category: String,
    val foundPlace: String,
    val foundDate: String,
    val content: String?,
    val author: String,
    val registeredAt: String,
    val updatedAt: String
)
