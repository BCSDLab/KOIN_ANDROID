package `in`.koreatech.koin.data.response.bus.v2

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.response.bus.CityBusInfoResponseV2
import `in`.koreatech.koin.domain.model.bus.CityBusInfoV2
import `in`.koreatech.koin.domain.model.bus.v2.CityTimetable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CityTimetableResponse(
    @SerializedName("bus_timetables") val timetable: List<CityTimetableItemResponse>?,
    @SerializedName("bus_info") val busInfo: CityBusInfoResponseV2?,
    @SerializedName("updated_at") val updatedAt: String?,
) {
    fun toCityTimetable() = CityTimetable(
        timetable = timetable?.map { it.toCityTimetableItem() }.orEmpty(),
        busInfo = busInfo?.toCityBusInfoV2() ?: CityBusInfoV2(0, "", ""),
        updatedAt = LocalDateTime.parse(updatedAt ?: "1999-04-29 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    )
}