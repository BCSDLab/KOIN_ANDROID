package `in`.koreatech.bus.mock

import `in`.koreatech.bus.screen.timetable.viewmodel.BusNoticeUiState
import `in`.koreatech.bus.state.BusNoticeState

internal val busNoticeUiStateMock = BusNoticeUiState.Show(
    BusNoticeState(
        id = 17153,
        title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
    )
)

internal val busNoticeMock = BusNoticeState(
    id = 17153,
    title = "[긴급] 9.27(금) 대학등교방향 천안셔틀버스 터미널 미정차 알림(천안역에서 승차바람)"
)