package `in`.koreatech.bus.mock

import `in`.koreatech.bus.state.ArrivalState
import `in`.koreatech.bus.state.CommonTimetableState
import `in`.koreatech.bus.state.ExpressTimetableState
import java.time.LocalDateTime

internal val commonTimetableMock = CommonTimetableState(
    amArrivals = listOf(
        ArrivalState("09:00"),
        ArrivalState("09:30"),
        ArrivalState("10:00"),
        ArrivalState("10:30"),
    ),
    pmArrivals = listOf(
        ArrivalState("14:30"),
        ArrivalState("21:00"),
        ArrivalState("21:30"),
        ArrivalState("22:00"),
        ArrivalState("22:30"),
        ArrivalState("23:00"),
        ArrivalState("23:30"),
    ),
)

internal val expressTimetableMock = ExpressTimetableState(
    timetable = commonTimetableMock,
    updatedAt = LocalDateTime.parse("2024-11-11T00:00:00"),
)