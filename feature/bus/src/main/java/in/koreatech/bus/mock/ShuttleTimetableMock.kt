package `in`.koreatech.bus.mock

import `in`.koreatech.bus.screen.shuttle_timetable.viewmodel.ShuttleTimetableUiState
import `in`.koreatech.bus.state.ShuttleTimetableNodeInfoState
import `in`.koreatech.bus.state.ShuttleTimetableRouteInfoState
import `in`.koreatech.bus.state.ShuttleTimetableState
import `in`.koreatech.bus.type.ShuttleBusOperationType

internal val shuttleTimetableNodeInfoMock1 =
    ShuttleTimetableNodeInfoState(
        name = "본교",
        detail = ""
    )
internal val shuttleTimetableNodeInfoMock2 =
    ShuttleTimetableNodeInfoState(
        name = "2캠퍼스",
        detail = ""
    )
internal val shuttleTimetableNodeInfoMock3 =
    ShuttleTimetableNodeInfoState(
        name = "천안터미널",
        detail = ""
    )
internal val shuttleTimetableNodeInfoMock4 =
    ShuttleTimetableNodeInfoState(
        name = "천안역",
        detail = ""
    )
internal val shuttleTimetableNodeInfoMock5 =
    ShuttleTimetableNodeInfoState(
        name = "본교",
        detail = "인문경영관"
    )

internal val shuttleTimetableRouteInfoMock1 =
    ShuttleTimetableRouteInfoState(
        name = "1회",
        detail = "등교",
        arrivalTimes =
        listOf(
            "11:10",
            "",
            "11:35",
            "11:40",
            "12:10"
        )
    )
internal val shuttleTimetableRouteInfoMock2 =
    ShuttleTimetableRouteInfoState(
        name = "2회",
        detail = "",
        arrivalTimes =
        listOf(
            "13:10",
            "",
            "13:35",
            "13:40",
            "14:10"
        )
    )
internal val shuttleTimetableRouteInfoMock3 =
    ShuttleTimetableRouteInfoState(
        name = "3회",
        detail = "",
        arrivalTimes =
        listOf(
            "14:10",
            "14:35",
            "14:42",
            "14:47",
            "15:15"
        )
    )
internal val shuttleTimetableRouteInfoMock4 =
    ShuttleTimetableRouteInfoState(
        name = "4회",
        detail = "",
        arrivalTimes =
        listOf(
            "16:10",
            "",
            "16:35",
            "16:40",
            "17:10"
        )
    )
internal val shuttleTimetableRouteInfoMock5 =
    ShuttleTimetableRouteInfoState(
        name = "5회",
        detail = "",
        arrivalTimes =
        listOf(
            "20:00",
            "",
            "20:25",
            "20:30",
            "21:00"
        )
    )
internal val shuttleTimetableRouteInfoMock6 =
    ShuttleTimetableRouteInfoState(
        name = "6회",
        detail = "",
        arrivalTimes =
        listOf(
            "21:00",
            "",
            "21:25",
            "21:30",
            "22:00"
        )
    )
internal val shuttleTimetableRouteInfoMock7 =
    ShuttleTimetableRouteInfoState(
        name = "목, 금 추가",
        detail = "",
        arrivalTimes =
        listOf(
            "14:10/16:30",
            "",
            "14:35/16:55",
            "14:40/17:00",
            "15:10/17:30"
        )
    )
internal val shuttleTimetableUiStateMock1 =
    ShuttleTimetableUiState.Success(
        ShuttleTimetableState(
            region = "천안",
            routeType = ShuttleBusOperationType.CIRCULATION,
            routeName = "천안 셔틀",
            subTitle = "토요일",
            nodeInfo =
            listOf(
                shuttleTimetableNodeInfoMock1,
                shuttleTimetableNodeInfoMock2,
                shuttleTimetableNodeInfoMock3,
                shuttleTimetableNodeInfoMock4,
                shuttleTimetableNodeInfoMock5
            ),
            routeInfo =
            listOf(
                shuttleTimetableRouteInfoMock1,
                shuttleTimetableRouteInfoMock2,
                shuttleTimetableRouteInfoMock3,
                shuttleTimetableRouteInfoMock4,
                shuttleTimetableRouteInfoMock5,
                shuttleTimetableRouteInfoMock6,
                shuttleTimetableRouteInfoMock7
            )
        )
    )

internal val shuttleTimetableUiStateMock2 =
    ShuttleTimetableUiState.Success(
        ShuttleTimetableState(
            region = "천안",
            routeType = ShuttleBusOperationType.CIRCULATION,
            routeName = "대학원",
            subTitle = "",
            nodeInfo =
            listOf(
                shuttleTimetableNodeInfoMock1,
                shuttleTimetableNodeInfoMock2,
                shuttleTimetableNodeInfoMock3,
                shuttleTimetableNodeInfoMock4,
                shuttleTimetableNodeInfoMock5
            ),
            routeInfo =
            listOf(
                shuttleTimetableRouteInfoMock1,
                shuttleTimetableRouteInfoMock2
            )
        )
    )
