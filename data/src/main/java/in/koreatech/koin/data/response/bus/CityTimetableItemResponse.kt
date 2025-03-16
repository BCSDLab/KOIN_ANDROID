package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.CityTimetableItem

data class CityTimetableItemResponse(
    @SerializedName("day_of_week") val dayOfWeek: String?,
    @SerializedName("depart_info") val departureTimes: List<String>?
) {
    fun toCityTimetableItem() =
        CityTimetableItem(
            dayOfWeek = dayOfWeek.orEmpty(),
            departureTimes = departureTimes.orEmpty()
        )
}
