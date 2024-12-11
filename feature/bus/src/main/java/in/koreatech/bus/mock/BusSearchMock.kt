package `in`.koreatech.bus.mock

import `in`.koreatech.bus.state.BusSearchResultState
import `in`.koreatech.bus.type.BusType
import java.time.LocalTime

internal val busSearchResultMock1 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(6, 10)
)
internal val busSearchResultMock2 = BusSearchResultState(
    busType = BusType.SHUTTLE,
    routeName = "400",
    departureTime = LocalTime.of(6, 30)
)
internal val busSearchResultMock3 = BusSearchResultState(
    busType = BusType.EXPRESS,
    routeName = "400",
    departureTime = LocalTime.of(6, 50)
)
internal val busSearchResultMock4 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(7, 5)
)
internal val busSearchResultMock5 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(7, 21)
)
internal val busSearchResultMock6 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(7, 37)
)
internal val busSearchResultMock7 = BusSearchResultState(
    busType = BusType.EXPRESS,
    routeName = "400",
    departureTime = LocalTime.of(7, 53)
)
internal val busSearchResultMock8 = BusSearchResultState(
    busType = BusType.SHUTTLE,
    routeName = "400",
    departureTime = LocalTime.of(8, 9)
)
internal val busSearchResultMock9 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(8, 25)
)
internal val busSearchResultMock10 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(8, 41)
)
internal val busSearchResultMock11 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(8, 57)
)
internal val busSearchResultMock12 = BusSearchResultState(
    busType = BusType.SHUTTLE,
    routeName = "400",
    departureTime = LocalTime.of(9, 13)
)
internal val busSearchResultMock13 = BusSearchResultState(
    busType = BusType.EXPRESS,
    routeName = "400",
    departureTime = LocalTime.of(9, 29)
)
internal val busSearchResultMock14 = BusSearchResultState(
    busType = BusType.CITY,
    routeName = "400",
    departureTime = LocalTime.of(9, 45)
)
internal val busSearchResultMock15 = BusSearchResultState(
    busType = BusType.SHUTTLE,
    routeName = "400",
    departureTime = LocalTime.of(10, 1)
)

internal val busSearchResultsMock = listOf(
    busSearchResultMock1,
    busSearchResultMock2,
    busSearchResultMock3,
    busSearchResultMock4,
    busSearchResultMock5,
    busSearchResultMock6,
    busSearchResultMock7,
    busSearchResultMock8,
    busSearchResultMock9,
    busSearchResultMock10,
    busSearchResultMock11,
    busSearchResultMock12,
    busSearchResultMock13,
    busSearchResultMock14,
    busSearchResultMock15
)