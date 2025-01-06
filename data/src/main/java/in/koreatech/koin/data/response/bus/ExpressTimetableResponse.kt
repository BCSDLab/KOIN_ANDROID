package `in`.koreatech.koin.data.response.bus

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.domain.model.bus.ExpressTimetable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ExpressTimetableResponse(
    @SerializedName("bus_timetables") val timetable: List<ExpressTimetableItemResponse>?,
    @SerializedName("updated_at") val updatedAt: String?,
) {
    fun toExpressTimetable() = ExpressTimetable(
        timetable = timetable?.map { it.toExpressTimetableItem() }.orEmpty(),
        updatedAt = LocalDateTime.parse(updatedAt ?: "1999-04-29 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    )
}
