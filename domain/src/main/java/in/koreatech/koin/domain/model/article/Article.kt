package `in`.koreatech.koin.domain.model.article

data class Article(
    val id: Int,
    val boardId: Int,
    val title: String,
    val content: String,
    val author: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String
)