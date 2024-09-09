package `in`.koreatech.koin.domain.model.article

data class Article(
    val header: ArticleHeader,
    val content: String,
    val prevArticleId: Int?,
    val nextArticleId: Int?,
    val attachments: List<Attachment>
)