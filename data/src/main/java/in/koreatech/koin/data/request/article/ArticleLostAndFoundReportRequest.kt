package `in`.koreatech.koin.data.request.article

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.article.ArticleLostAndFoundReportItem

data class ArticleLostAndFoundReportRequest(
    @SerializedName("reports")
    val reports: List<ArticleLostAndFoundReportRequestItem>
)

data class ArticleLostAndFoundReportRequestItem(
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
)

fun List<ArticleLostAndFoundReportItem>.toRequest(): ArticleLostAndFoundReportRequest {
    return ArticleLostAndFoundReportRequest(
        reports = map {
            ArticleLostAndFoundReportRequestItem(
                title = it.title,
                content = it.content,
            )
        }
    )
}