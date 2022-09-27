package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class NextBusResponse(
    @SerializedName("next_bus_number") val nextBusNumber: Int,
    @SerializedName("remain_time") val remainTime: Int,
    @SerializedName("bus_number") val busNumber: Int,
    @SerializedName("next_remain_time") val nextRemainTime: Int
)