package `in`.koreatech.koin.domain.model.article

import `in`.koreatech.koin.domain.model.article.html.HtmlModel

data class Article(
    val id: Int,
    val boardId: Int,
    val title: String,
    val html: HtmlModel,
    val author: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String
)