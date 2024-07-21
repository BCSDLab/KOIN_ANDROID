package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreReviewResponse (
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("current_count") val currentCount: Int,
    @SerializedName("total_page") val totalPage: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("statistics") val statistics: StoreReviewStatisticsResponse,
    @SerializedName("reviews") val reviews: List<StoreReviewContentResponse>
)