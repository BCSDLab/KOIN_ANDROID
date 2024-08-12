package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.domain.model.article.ArticlePagination

data class ArticlePaginationResponse(
    @SerializedName("articles") val articles: List<ArticleResponse>,
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int
) {
    fun toArticlePagination() = ArticlePagination(
        articles = articles.map { it.toArticle() },
        totalCount = totalCount,
        currentCount = currentCount,
        totalPage = totalPage,
        currentPage = currentPage
    )
}