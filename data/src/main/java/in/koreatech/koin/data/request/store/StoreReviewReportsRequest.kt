package `in`.koreatech.koin.data.request.store

import com.google.gson.annotations.SerializedName

data class StoreReviewReportsRequest(
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String
)