package `in`.koreatech.koin.domain.model.article

data class ArticleHeader(
    val id: Int,
    val boardId: Int,
    val title: String,
    val author: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String,
)