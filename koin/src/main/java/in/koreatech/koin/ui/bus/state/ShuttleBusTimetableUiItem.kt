package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import android.content.Context
import java.time.format.DateTimeFormatter

data class ShuttleBusTimetableUiItem(
    val node: String,
    val arrivalTime: String
)

fun BusNodeInfo.ShuttleNodeInfo.toShuttleBusTimetableUiItem() = ShuttleBusTimetableUiItem(
    node = node,
    arrivalTime = arrivalTime
)