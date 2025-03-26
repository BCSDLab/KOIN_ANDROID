package `in`.koreatech.koin.domain.model.article

data class ArticleLostAndFound(
    val id: Int,
    val boardId: Int,
    val type: String,
    val category: String,
    val foundPlace: String,
    val foundDate: String,
    val content: String?,
    val author: String,
    val isCouncil: Boolean,
    val isMine: Boolean,
    val images: List<ArticleLostAndFoundImage>?,
    val registeredAt: String,
    val updatedAt: String
) {
    data class ArticleLostAndFoundImage(
        val id: Int,
        val imageUrl: String
    )
}
