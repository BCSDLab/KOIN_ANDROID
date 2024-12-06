package `in`.koreatech.bus.viewstate

import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableNodeInfo

data class ShuttleTimetableNodeInfoState(
    val name: String,
    val detail: String,
)

fun ShuttleTimetableNodeInfo.toShuttleTimetableNodeInfoState() = ShuttleTimetableNodeInfoState(
    name = name,
    detail = detail
)