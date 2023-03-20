package `in`.koreatech.core.networking.response.bus

import com.google.gson.annotations.SerializedName

data class BusSearchResponse(
    @SerializedName("bus_name") val busName: String,
    @SerializedName("bus_time") val busTime: String?
)
