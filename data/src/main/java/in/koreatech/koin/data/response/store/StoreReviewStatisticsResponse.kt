package `in`.koreatech.koin.data.response.store

import com.google.gson.annotations.SerializedName

data class StoreReviewStatisticsResponse (
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("ratings") val ratings: Map<String, Int>
)