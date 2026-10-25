package `in`.koreatech.koin.data.request.dining

import com.google.gson.annotations.SerializedName

data class DiningSoldOutReportRequest(
    @SerializedName("image_url") val imageUrl: String
)
