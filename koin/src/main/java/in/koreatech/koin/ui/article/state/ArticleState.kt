package `in`.koreatech.koin.ui.article.state

import `in`.koreatech.koin.domain.model.article.Article

data class ArticleState(
    val header: ArticleHeaderState,
    val content: String,
    val prevArticleId: Int?,
    val nextArticleId: Int?,
    val attachments: List<AttachmentState>
)

fun Article.toArticleState() = ArticleState(
    header = header.toArticleHeaderState(),
    content = content,
    prevArticleId = prevArticleId,
    nextArticleId = nextArticleId,
    attachments = attachments.map { it.toAttachmentState() }
)