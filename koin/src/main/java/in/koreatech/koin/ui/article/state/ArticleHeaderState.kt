package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.ui.article.BoardType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleHeaderState(
    val id: Int,
    val boardId: Int,
    @StringRes val boardName: Int?,
    val title: String,
    val author: String,
    val viewCount: Int,
    val createdAt: String,
    val updatedAt: String,
) : Parcelable

fun ArticleHeader.toArticleHeaderState() = ArticleHeaderState(
    id = id,
    boardId = boardId,
    boardName = BoardType.entries.find { it.id == boardId }?.koreanName,
    title = title,
    author = author,
    viewCount = viewCount,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
