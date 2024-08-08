package `in`.koreatech.koin.data.request.store

import com.google.gson.annotations.SerializedName

data class StoreReviewReportsRequest(
    @SerializedName("reports") val reports: List<ReportContent>
){
    data class ReportContent(
        @SerializedName("title") val title: String,
        @SerializedName("content") val content: String
    )
}