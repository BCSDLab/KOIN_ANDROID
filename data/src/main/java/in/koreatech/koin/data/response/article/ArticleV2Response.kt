package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.Article
import `in`.koreatech.koin.domain.model.article.ArticleAiSummary
import `in`.koreatech.koin.domain.model.article.ArticleHeader

data class ArticleV2Response(
    @SerializedName("id") val id: Int?,
    @SerializedName("board_id") val boardId: Int?,
    @SerializedName("title") val title: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("ai_summary") val aiSummary: AiSummaryResponse?,
    @SerializedName("author") val author: String?,
    @SerializedName("hit") val viewCount: Int?,
    @SerializedName("registered_at") val registeredAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("prev_id") val prevArticleId: Int?,
    @SerializedName("next_id") val nextArticleId: Int?,
    @SerializedName("attachments") val attachments: List<AttachmentResponse>?,
    @SerializedName("url") val url: String?
) {
    fun toArticleHeader() =
        ArticleHeader(
            id = id ?: 0,
            boardId = boardId ?: 0,
            title = title ?: "",
            author = author ?: "",
            viewCount = viewCount ?: 0,
            registeredAt = registeredAt ?: "",
            updatedAt = updatedAt ?: ""
        )

    fun toArticle() =
        Article(
            header = toArticleHeader(),
            aiSummary = aiSummary?.toArticleAiSummary(),
            content = content ?: "",
            prevArticleId = prevArticleId,
            nextArticleId = nextArticleId,
            attachments = attachments?.map { it.toAttachment() } ?: listOf(),
            url = url ?: ""
        )
}

data class AiSummaryResponse(
    @SerializedName("status") val status: String,
    @SerializedName("items") val items: List<SummaryItem>
) {
    fun toArticleAiSummary() =
        ArticleAiSummary(
            status = status,
            summaryItems = items.toSummaryItems()
        )

    data class SummaryItem(
        @SerializedName("icon") val icon: String,
        @SerializedName("text") val text: String
    ) {
        fun toSummaryItem() =
            ArticleAiSummary.SummaryItem(
                icon = icon,
                text = text
            )
    }

    private fun List<SummaryItem>.toSummaryItems() =
        this.map { it.toSummaryItem() }
}
