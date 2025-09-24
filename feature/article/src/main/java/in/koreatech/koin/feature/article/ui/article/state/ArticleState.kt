package `in`.koreatech.koin.feature.article.ui.article.state

import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.feature.article.model.ArticleHeaderState
import `in`.koreatech.koin.feature.article.model.toArticleHeaderState

data class ArticleState(
    val header: ArticleHeaderState,
    val content: String,
    val prevArticleId: Int?,
    val nextArticleId: Int?,
    val attachments: List<AttachmentState>,
    val url: String
)

fun Article.toArticleState() = ArticleState(
    header = header.toArticleHeaderState(),
    content = content,
    prevArticleId = prevArticleId,
    nextArticleId = nextArticleId,
    attachments = attachments.map { it.toAttachmentState() },
    url = url
)
