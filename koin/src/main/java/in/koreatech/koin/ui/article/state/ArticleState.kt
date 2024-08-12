package `in`.koreatech.koin.ui.article.state

import android.os.Parcelable
import `in`.koreatech.koin.domain.model.article.Article
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleState(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val views: Int,
    val createdAt: String,
    val updatedAt: String
) : Parcelable {

    fun Article.toArticleState() = ArticleState(
        id = id,
        title = title,
        content = content,
        author = author,
        views = views,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
