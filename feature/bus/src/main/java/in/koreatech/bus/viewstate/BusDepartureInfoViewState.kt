package `in`.koreatech.bus.viewstate

import `in`.koreatech.bus.screen.timetable.type.BusType

// TODO : 임시 데이터 타입
data class BusDepartureInfoViewState(
    val type: BusType,
    val departureTime: String,
)
