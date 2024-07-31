package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName

data class CityBusTimetableResponse(
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("bus_info") val busInfo: CityBusInfoResponse,
    @SerializedName("bus_timetables") val departTimes: List<CityBusDepartTimesResponse>
)

data class CityBusInfoResponse(
    @SerializedName("number") val number: Int,
    @SerializedName("depart_node") val departNode: String,
    @SerializedName("arrival_node") val arrivalNode: String
)

data class CityBusDepartTimesResponse(
    @SerializedName("day_of_week") val dayOfWeek: String,
    @SerializedName("depart_info") val departInfo: List<String>
)