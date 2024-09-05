package `in`.koreatech.koin.domain.model.article

import `in`.koreatech.koin.domain.model.article.html.HtmlModel

data class Article(
    val header: ArticleHeader,
    val content: HtmlModel,
    val prevArticleId: Int?,
    val nextArticleId: Int?,
    val attachments: List<Attachment>
)