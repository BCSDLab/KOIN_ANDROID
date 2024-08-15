package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.response.article.ArticleResponse
import `in`.koreatech.koin.domain.model.article.ArticlePagination

data class ArticlePaginationResponse(
    @SerializedName("articles") val articles: List<ArticleResponse>,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("currentCount") val currentCount: Int,
    @SerializedName("totalPage") val totalPage: Int,
    @SerializedName("currentPage") val currentPage: Int
) {
    fun toArticlePagination() = ArticlePagination(
        articles = articles.map { it.toArticle() },
        totalCount = totalCount,
        currentCount = currentCount,
        totalPage = totalPage,
        currentPage = currentPage
    )
}