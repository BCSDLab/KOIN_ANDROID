package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class BusCourseResponse(
    @SerializedName("bus_type") val busType: String,
    @SerializedName("id") val id: String,
    @SerializedName("region") val region: String
)
