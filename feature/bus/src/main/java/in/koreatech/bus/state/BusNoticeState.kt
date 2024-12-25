package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice

data class BusNoticeState(
    val id: Int,
    val title: String,
)

fun BusNotice.toBusNoticeState() = BusNoticeState(
    id = id,
    title = title,
)