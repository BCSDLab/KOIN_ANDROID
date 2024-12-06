package `in`.koreatech.bus.viewstate

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice

data class BusNoticeViewState(
    val id: Int,
    val title: String,
)

fun BusNotice.toBusNoticeViewState() = BusNoticeViewState(
    id = id,
    title = title,
)