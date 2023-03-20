package `in`.koreatech.core.networking.response.bus

import com.google.gson.annotations.SerializedName

data class BusCourseResponse(
    @SerializedName("bus_type") val busType: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("region") val region: String
)
