package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.ui.article.BoardType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleState(
    val id: Int,
    val boardId: Int,
    @StringRes val boardName: Int,
    val title: String,
    val content: String,
    val author: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String
) : Parcelable

fun Article.toArticleState() = ArticleState(
    id = id,
    boardId = boardId,
    boardName = BoardType.entries.find { it.id == boardId }?.koreanName ?: throw IllegalArgumentException("Unable to find board name"),
    title = title,
    content = content,
    author = author,
    viewCount = viewCount,
    createdAt = createdAt,
    updatedAt = updatedAt
)
