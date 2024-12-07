package `in`.koreatech.koin.data.response.bus.v2

import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class ExpressTimetableResponse(
    @SerialName("bus_timetables") val timetable: List<ExpressTimetableItemResponse>?,
    @SerialName("updated_at") val updatedAt: String?,
) {
    fun toExpressTimetable() = ExpressTimetable(
        timetable = timetable?.map { it.toExpressTimetableItem() }.orEmpty(),
        updatedAt = LocalDateTime.parse(updatedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    )
}
