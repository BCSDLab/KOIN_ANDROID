package `in`.koreatech.bus.mock

import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.bus.state.ShuttleCourseRegionState
import `in`.koreatech.bus.state.ShuttleCourseRouteState
import `in`.koreatech.bus.state.ShuttleCoursesState
import `in`.koreatech.bus.state.ShuttleSemesterState
import java.time.LocalDate

internal val shuttleSemesterMock = ShuttleSemesterState(
    name = "정규학기",
    from = LocalDate.parse("2024-09-01"),
    to = LocalDate.parse("2024-12-21")
)

internal val shuttleCourseRouteMock1 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.CIRCULATION,
    routeName = "천안셔틀",
    subName = ""
)
internal val shuttleCourseRouteMock2 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "천안역",
    subName = ""
)
internal val shuttleCourseRouteMock3 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "터미널",
    subName = ""
)
internal val shuttleCourseRouteMock4 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "두정역",
    subName = ""
)
internal val shuttleCourseRouteMock5 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "아산 / KTX",
    subName = ""
)
internal val shuttleCourseRouteMock6 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKEND,
    routeName = "천안셔틀",
    subName = "토요일, 일요일"
)
internal val shuttleCourseRouteMock7 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKEND,
    routeName = "일학습병행대학 시내",
    subName = "토요일"
)
internal val shuttleCourseRouteMock8 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKEND,
    routeName = "일학습병행대학 천안아산역",
    subName = "토요일"
)
internal val shuttleCourseRouteMock9 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.CIRCULATION,
    routeName = "청주셔틀",
    subName = ""
)
internal val shuttleCourseRouteMock10 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "용암동",
    subName = ""
)
internal val shuttleCourseRouteMock11 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "동남지구",
    subName = ""
)
internal val shuttleCourseRouteMock12 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "산남/분평",
    subName = ""
)
internal val shuttleCourseRouteMock13 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "서울 교대역",
    subName = ""
)
internal val shuttleCourseRouteMock14 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "서울 교대역",
    subName = "월요일"
)
internal val shuttleCourseRouteMock15 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "서울 동천역",
    subName = "월요일"
)
internal val shuttleCourseRouteMock16 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "서울 하교 1",
    subName = "금요일"
)
internal val shuttleCourseRouteMock17 = ShuttleCourseRouteState(
    id = "1",
    type = ShuttleBusOperationType.WEEKDAY,
    routeName = "서울 하교 2",
    subName = "금요일"
)

internal val shuttleCoursesMock = ShuttleCoursesState(
    courses = mapOf(
        ShuttleCourseRegionState("천안・아산") to listOf(
            shuttleCourseRouteMock1,
            shuttleCourseRouteMock2,
            shuttleCourseRouteMock3,
            shuttleCourseRouteMock4,
            shuttleCourseRouteMock5,
            shuttleCourseRouteMock6,
            shuttleCourseRouteMock7,
            shuttleCourseRouteMock8
        ), ShuttleCourseRegionState("청주") to listOf(
            shuttleCourseRouteMock9,
            shuttleCourseRouteMock10,
            shuttleCourseRouteMock11,
            shuttleCourseRouteMock12
        ), ShuttleCourseRegionState("서울") to listOf(
            shuttleCourseRouteMock13,
            shuttleCourseRouteMock14,
            shuttleCourseRouteMock15,
            shuttleCourseRouteMock16,
            shuttleCourseRouteMock17
        )
    ),
    semester = shuttleSemesterMock
)