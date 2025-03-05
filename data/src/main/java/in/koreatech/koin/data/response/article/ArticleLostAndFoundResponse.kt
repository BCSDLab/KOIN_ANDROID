package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader

data class ArticleLostAndFoundResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("board_id") val boardId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("category") val category: String,
    @SerializedName("found_place") val foundPlace: String,
    @SerializedName("found_date") val foundDate: String,
    @SerializedName("content") val content: String?,
    @SerializedName("author") val author: String,
    @SerializedName("is_council") val isCouncil: Boolean?, // Set nullable because /articles/lost-item API doesn't have this field
    @SerializedName("is_mine") val isMine: Boolean?, // Set nullable because /articles/lost-item API doesn't have this field
    @SerializedName("is_reported") val isReported: Boolean,
    @SerializedName("images") val images: List<ArticleLostAndFoundImageResponse>?,
    @SerializedName("prev_id") val prevArticleId: Int?,
    @SerializedName("next_id") val nextArticleId: Int?,
    @SerializedName("registered_at") val registeredAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
) {
    data class ArticleLostAndFoundImageResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("image_url") val imageUrl: String,
    ) {
        fun toArticleLostAndFoundImage() =
            ArticleLostAndFound.ArticleLostAndFoundImage(
                id = id,
                imageUrl = imageUrl,
            )
    }

    fun toArticleLostAndFoundHeader() =
        ArticleLostAndFoundHeader(
            id = id,
            boardId = boardId,
            type = type,
            category = category,
            foundPlace = foundPlace,
            foundDate = foundDate,
            content = content,
            author = author,
            isReported = isReported,
            registeredAt = registeredAt,
            updatedAt = updatedAt ?: "", // updatedAt is not available on /articles/lost-item API
        )

    /*
     * Convert ArticleLostAndFoundResponse to ArticleLostAndFound
     * For /articles/lost-item/{id} GET API
     * For /articles/lost-item POST API
     */
    fun toArticleLostAndFound() =
        ArticleLostAndFound(
            id = id,
            boardId = boardId,
            type = type,
            category = category,
            foundPlace = foundPlace,
            foundDate = foundDate,
            content = content,
            author = author,
            isCouncil = isCouncil!!, // Should not be null
            isMine = isMine!!, // Should not be null
            images = images?.map { it.toArticleLostAndFoundImage() },
            registeredAt = registeredAt,
            updatedAt = updatedAt ?: "", // updatedAt is not available on /articles/lost-item API
        )
}
