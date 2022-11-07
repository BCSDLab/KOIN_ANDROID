package `in`.koreatech.koin.ui.bus.state

import `in`.koreatech.koin.R
import `in`.koreatech.koin.domain.model.bus.timetable.BusNodeInfo
import android.content.Context

data class CityBusTimetableUiItem(
    val startLocation: String,
    val timeInfo: String
)

fun BusNodeInfo.CitybusNodeInfo.toCityBusTimetableUiItem() = CityBusTimetableUiItem(
    startLocation = startLocation,
    timeInfo = timeInfo
)