package `in`.koreatech.koin.data.response.bus.shuttle

import com.google.gson.annotations.SerializedName

data class ArrivalInfoResponse(
    @SerializedName("node_name") val nodeName: String,
    @SerializedName("arrival_time") val arrivalTime: String
)
