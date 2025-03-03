package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.ShuttleTimetableNodeInfo

data class ShuttleTimetableNodeInfoState(
    val name: String,
    val detail: String,
)

fun ShuttleTimetableNodeInfo.toShuttleTimetableNodeInfoState() =
    ShuttleTimetableNodeInfoState(
        name = name,
        detail = detail,
    )
