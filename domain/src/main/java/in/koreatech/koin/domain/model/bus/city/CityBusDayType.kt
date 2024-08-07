package `in`.koreatech.koin.domain.model.bus.city

import java.time.DayOfWeek

enum class CityBusDayType(
    val type: String,
    // TODO: 공휴일, 임시 조건 추가하면서 타입 변경 필요
    val daysOfWeek: List<DayOfWeek>
) {
    Weekday("평일", listOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)),
    Weekend("주말", listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)),
    // TODO: 조건 추가
    Holiday("공휴일", emptyList()),
    Temp("임시", emptyList())
}

val String.toCityBusDayType
    get() = when (this) {
        "평일" -> CityBusDayType.Weekday
        "주말" -> CityBusDayType.Weekend
        "공휴일" -> CityBusDayType.Holiday
        "임시" -> CityBusDayType.Temp
        else -> throw IllegalArgumentException("Invalid day type")
    }