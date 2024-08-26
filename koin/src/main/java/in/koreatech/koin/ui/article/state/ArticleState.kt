package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.Article

data class ArticleState(
    val header: ArticleHeaderState,
    val content: HtmlElement,
    val prevArticleId: Int?,
    val nextArticleId: Int?
)

fun Article.toArticleState() = ArticleState(
    header = header.toArticleHeaderState(),
    content = content.toHtmlElement(),
    prevArticleId = prevArticleId,
    nextArticleId = nextArticleId
)