package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.ui.article.ArticleBoardType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleHeaderState(
    val id: Int,
    val board: ArticleBoardType,
    val title: String,
    val author: String,
    val viewCount: Int,
    val registeredAt: String,
    val updatedAt: String,
) : Parcelable

fun ArticleHeader.toArticleHeaderState() = ArticleHeaderState(
    id = id,
    board = ArticleBoardType.entries.firstOrNull { it.id == boardId } ?: ArticleBoardType.ALL,
    title = title,
    author = author,
    viewCount = viewCount,
    registeredAt = registeredAt,
    updatedAt = updatedAt,
)
