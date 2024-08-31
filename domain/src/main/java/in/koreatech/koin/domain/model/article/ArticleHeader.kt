package `in`.koreatech.koin.domain.model.article

data class ArticleHeader(
    val id: Int,
    val boardId: Int,
    val title: String,
    val author: String,
    val viewCount: Int,
    val registeredAt: String,
    val updatedAt: String,
)