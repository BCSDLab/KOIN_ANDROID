package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class BusRemainTimeResponse(
    @SerializedName("bus_number") val busNumber: Int? = null,
    @SerializedName("remain_time") val remainTimeSecond: Int
)
