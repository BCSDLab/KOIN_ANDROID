package `in`.koreatech.bus.state

import `in`.koreatech.bus.type.BusType

// TODO : 임시 데이터 타입
data class BusDepartureInfoViewState(
    val type: BusType,
    val departureHour: Int,
    val departureMinute: Int,
    val remainingTime: Int, // 남은 시간(실제 현재 시간 기준)
)
