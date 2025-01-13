package `in`.koreatech.koin.data.request.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.response.article.ArticleLostAndFoundResponse.ArticleLostAndFoundImageResponse

data class ArticleLostAndFoundRequest(
    @SerializedName("articles") val articles: List<ArticleLostAndFoundBody>,
) {
    data class ArticleLostAndFoundBody(
        @SerializedName("category") val category: String,
        @SerializedName("location") val location: String,
        @SerializedName("found_date") val foundDate: String,
        @SerializedName("content") val content: String?,
        @SerializedName("images") val images: List<String>?
    )
}