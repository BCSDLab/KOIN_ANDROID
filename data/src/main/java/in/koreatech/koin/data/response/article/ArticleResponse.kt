package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.Article

data class ArticleResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("board_id") val boardId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("nickname") val author: String?,
    @SerializedName("hit") val viewCount: Int?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
) {
    fun toArticle() = Article(
        id = id ?: 0,
        boardId = boardId ?: 0,
        title = title ?: "",
        content = content ?: "",
        author = author ?: "",
        viewCount = viewCount ?: 0,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: ""
    )
}
