package `in`.koreatech.bus.state

import `in`.koreatech.koin.domain.model.bus.BusNotice

data class BusNoticeState(
    val id: Int,
    val title: String,
)

fun BusNotice.toBusNoticeState() =
    BusNoticeState(
        id = id,
        title = title,
    )
