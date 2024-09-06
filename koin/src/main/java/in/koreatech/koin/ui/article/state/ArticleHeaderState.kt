package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import androidx.annotation.StringRes
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.ui.article.BoardType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleHeaderState(
    val id: Int,
    val board: BoardType,
    val title: String,
    val author: String,
    val viewCount: Int,
    val registeredAt: String,
    val updatedAt: String,
) : Parcelable

fun ArticleHeader.toArticleHeaderState() = ArticleHeaderState(
    id = id,
    board = BoardType.entries.firstOrNull { it.id == boardId } ?: BoardType.ALL,
    title = title,
    author = author,
    viewCount = viewCount,
    registeredAt = registeredAt,
    updatedAt = updatedAt,
)
