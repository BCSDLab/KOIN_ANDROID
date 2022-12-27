package `in`.koreatech.koin.data.response.bus

import `in`.koreatech.koin.domain.model.bus.BusDirection
import com.google.gson.annotations.SerializedName

data class BusCourseResponse(
    @SerializedName("bus_type") val busType: String,
    @SerializedName("direction") val direction: String,
    @SerializedName("region") val region: String
)
