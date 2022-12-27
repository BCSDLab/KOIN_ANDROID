package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class BusResponse(
    @SerializedName("bus_type") val busType: String,
    @SerializedName("now_bus") val nowBus: BusRemainTimeResponse? = null,
    @SerializedName("next_bus") val nextBus: BusRemainTimeResponse? = null
)