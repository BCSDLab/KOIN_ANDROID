package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundPagination

data class ArticleLostAndFoundPaginationResponse(
    @SerializedName("articles") val articles: List<ArticleLostAndFoundResponse>,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int
) {
    fun toArticleLostAndFoundPagination() =
        ArticleLostAndFoundPagination(
            articleLostAndFoundHeader = articles.map { it.toArticleLostAndFoundHeader() },
            totalCount = totalCount,
            currentCount = currentCount,
            totalPage = totalPage,
            currentPage = currentPage
        )
}
