package `in`.koreatech.koin.domain.model.bus

data class CityTimetableItem(
    val dayOfWeek: String,
    val departureTimes: List<String>
)
