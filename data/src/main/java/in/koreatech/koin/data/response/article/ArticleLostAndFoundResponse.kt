package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFound
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundHeader

data class ArticleLostAndFoundResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("board_id") val boardId: Int,
    @SerializedName("category") val category: String,
    @SerializedName("found_place") val foundPlace: String,
    @SerializedName("found_date") val foundDate: String,
    @SerializedName("content") val content: String?,
    @SerializedName("author") val author: String,
    @SerializedName("image") val images: List<ArticleLostAndFoundImageResponse>?, // TODO: Fix after API is fixed
    @SerializedName("prev_id") val prevArticleId: Int?,
    @SerializedName("next_id") val nextArticleId: Int?,
    @SerializedName("registered_at") val registeredAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
) {
    data class ArticleLostAndFoundImageResponse(
        @SerializedName("id") val id: Int,
        @SerializedName("image_url") val imageUrl: String,
    ) {
        fun toArticleLostAndFoundImage() = ArticleLostAndFound.ArticleLostAndFoundImage(
            id = id,
            imageUrl = imageUrl
        )
    }

    fun toArticleLostAndFoundHeader() = ArticleLostAndFoundHeader(
        id = id,
        boardId = boardId,
        category = category,
        foundPlace = foundPlace,
        foundDate = foundDate,
        content = content,
        author = author,
        registeredAt = registeredAt,
        updatedAt = updatedAt ?: "", // updatedAt is not available on /articles/lost-item API
    )

    fun toArticleLostAndFound() = ArticleLostAndFound(
        id = id,
        boardId = boardId,
        category = category,
        foundPlace = foundPlace,
        foundDate = foundDate,
        content = content,
        author = author,
        images = images?.map { it.toArticleLostAndFoundImage() },
        registeredAt = registeredAt,
        updatedAt = updatedAt ?: "", // updatedAt is not available on /articles/lost-item API
    )
}