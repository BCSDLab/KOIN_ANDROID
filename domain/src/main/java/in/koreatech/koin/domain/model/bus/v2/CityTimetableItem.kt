package `in`.koreatech.koin.domain.model.bus.v2

data class CityTimetableItem(
    val dayOfWeek: String,
    val departureTimes: List<String>
)
