package `in`.koreatech.koin.feature.article.model

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.ArticleHeader
import `in`.koreatech.koin.feature.article.enums.ArticleBoardType
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

/**
 * For hot articles
 */
@Parcelize
data class ArticleHeaderState(
    val id: Int,
    val board: ArticleBoardType,
    val title: String,
    val author: String,
    val viewCount: Int,
    val registeredAt: LocalDate,
    val updatedAt: String
) : Parcelable

fun ArticleHeader.toArticleHeaderState() =
    ArticleHeaderState(
        id = id,
        board = ArticleBoardType.entries.firstOrNull { it.id == boardId } ?: ArticleBoardType.ALL,
        title = title,
        author = author,
        viewCount = viewCount,
        registeredAt = LocalDate.parse(registeredAt),
        updatedAt = updatedAt
    )
