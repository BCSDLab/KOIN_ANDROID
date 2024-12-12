package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.v2.BusSearchResult
import java.time.LocalTime

data class BusSearchResultWrapperResponse(
    @SerializedName("depart") val departure: String?,
    @SerializedName("arrival") val arrival: String?,
    @SerializedName("depart_date") val departDate: String?,
    @SerializedName("depart_time") val departTime: String?,
    @SerializedName("schedule") val schedules: List<BusSearchResultResponse>?,
)

data class BusSearchResultResponse(
    @SerializedName("bus_type") val busType: String?,
    @SerializedName("bus_name") val busName: String?,
    @SerializedName("depart_time") val departureTime: String?,
) {
    fun toBusSearchResult() = BusSearchResult(
        busType = busType.orEmpty(),
        busName = busName.orEmpty(),
        departureTime = LocalTime.parse(departureTime)
    )
}