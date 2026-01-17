package `in`.koreatech.koin.data.response.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundStats

data class ArticleLostAndFoundStatsResponse(
    @SerializedName("found_count") val foundCount: Int,
    @SerializedName("not_found_count") val notFoundCount: Int
) {
    fun toArticleLostAndFoundStats() = ArticleLostAndFoundStats(
        foundCount = foundCount,
        notFoundCount = notFoundCount
    )
}
