package `in`.koreatech.bus.mock

import `in`.koreatech.bus.state.CityBusInfoState
import `in`.koreatech.bus.state.CityTimetableState
import `in`.koreatech.bus.state.CommonTimetableState
import `in`.koreatech.bus.state.DepartureState
import `in`.koreatech.bus.state.ExpressTimetableState
import java.time.LocalDateTime

internal val commonTimetableMock =
    CommonTimetableState(
        amDepartures =
            listOf(
                DepartureState("09:00"),
                DepartureState("09:30"),
                DepartureState("10:00"),
                DepartureState("10:30"),
            ),
        pmDepartures =
            listOf(
                DepartureState("14:30"),
                DepartureState("21:00"),
                DepartureState("21:30"),
                DepartureState("22:00"),
                DepartureState("22:30"),
                DepartureState("23:00"),
                DepartureState("23:30"),
            ),
    )

internal val expressTimetableMock =
    ExpressTimetableState(
        timetable = commonTimetableMock,
        updatedAt = LocalDateTime.parse("2024-11-11T00:00:00"),
    )

internal val cityTimetableMock =
    CityTimetableState(
        departureTimes = commonTimetableMock,
        busInfo =
            CityBusInfoState(
                number = 0,
                departNode = "",
                arriveNode = "",
            ),
        updatedAt = LocalDateTime.parse("2024-11-11T00:00:00"),
    )
